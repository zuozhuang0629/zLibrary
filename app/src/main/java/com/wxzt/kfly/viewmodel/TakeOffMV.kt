package com.wxzt.kfly.viewmodel

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.wxzt.kfly.dji.DJICameraHelper
import com.wxzt.kfly.dji.DJIDeviceHelper
import com.wxzt.kfly.entitys.TakeOffInfoEntity
import com.wxzt.lib_common.enums.DetectionViewType
import com.wxzt.lib_common.rx.RxBus
import com.wxzt.lib_common.viewmodel.BaseViewModel
import dji.common.battery.BatteryState
import dji.common.camera.SettingsDefinitions
import dji.common.camera.StorageState
import dji.common.flightcontroller.FlightControllerState
import dji.common.flightcontroller.LocationCoordinate3D
import dji.common.mission.waypoint.WaypointMissionUploadEvent
import dji.common.model.LocationCoordinate2D
import dji.common.remotecontroller.HardwareState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 *  author      : zuoz
 *  date        : 2021/4/14 14:55
 *  description :
 */
class TakeOffMV : BaseViewModel() {
    private val mCompositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    val isConnected: MutableLiveData<TakeOffInfoEntity> = MutableLiveData()
    val BATTARY: MutableLiveData<TakeOffInfoEntity> = MutableLiveData()
    val MISSION: MutableLiveData<TakeOffInfoEntity> = MutableLiveData()
    val GPS: MutableLiveData<TakeOffInfoEntity> = MutableLiveData()
    val STORAGE: MutableLiveData<TakeOffInfoEntity> = MutableLiveData()
    val DISTANC: MutableLiveData<TakeOffInfoEntity> = MutableLiveData()
    val HARDWARE: MutableLiveData<TakeOffInfoEntity> = MutableLiveData()

    private var cameraModel = false
    private var photoFormat = false
    private var photoAspectRatio = false
    private var goHomeLocation: LocationCoordinate2D? = null
    private var currentLocation: LocationCoordinate3D? = null

    fun startOff() {
        isConnect()
        initListener()
        setCamera()
//        loadTask()
    }

    private fun isConnect() {
        addDisposable(Observable.just("")
                .delay(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val isCanFlyByConnect = DJIDeviceHelper.instance.isConnected
                    isConnected.value = TakeOffInfoEntity(if (isCanFlyByConnect) DetectionViewType.OK else DetectionViewType.WARRAY,
                            if (isCanFlyByConnect) "??????????????????" else "?????????????????????")
                })
    }

//    fun loadTask() {
//        val tasks: List<LatLng> = mBaseMissionInfoEntity.getTasks()
//        if (tasks.size == 0) {
//            iView.changeMission(DetectionViewType.WARRAY, "??????????????????:???????????????")
//            return
//        }
//        val baseFlySettingEntity: BasePlanningSettingEntity = mBaseSettingEntity.getFlySetting()
//        if (null == tasks || null == baseFlySettingEntity) {
//            isCanFlyByMission = false
//            return
//        }
//        val djiWayMissionHelper: DJIWayMissionHelper = DJIWayMissionHelper.getInstance()
//        if (null == djiWayMissionHelper) {
//            isCanFlyByMission = false
//            if (null == iView) {
//                return
//            }
//            iView.changeMission(DetectionViewType.WARRAY, "??????????????????")
//        }
//        val waypointMission: WaypointMission = mDjiMission.getMission(mBaseSettingEntity,
//                Math.max(mBaseMissionInfoEntity.getDoneProgress(), 0))
//        addDisposable(
//                Observable.timer(4, TimeUnit.SECONDS)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(Consumer {
//                            val djiError: DJIError = djiWayMissionHelper.LoadMission(waypointMission)
//                            if (null == djiError) {
//                                djiWayMissionHelper.uploadTask()
//                                if (null == iView) {
//                                    return@Consumer
//                                }
//                                iView.changeMission(DetectionViewType.NORMAL, "?????????????????????...")
//                            } else {
//                                isCanFlyByMission = false
//                                if (null == iView) {
//                                    return@Consumer
//                                }
//                                iView.changeMission(DetectionViewType.WARRAY, "??????????????????$djiError")
//                            }
//                        }))
//    }

    /**
     * ???????????????
     */
    private fun initListener() {
        RxBus.INSTANCE.doDefaultSubscribe(BatteryState::class.java, Consumer { batteryState ->
            val battery = batteryState.chargeRemainingInPercent
            val isCanFlyByBattary = battery >= 30
            ToastUtils.showLong("1111")
            BATTARY.value = TakeOffInfoEntity(if (isCanFlyByBattary) DetectionViewType.OK else DetectionViewType.WARRAY,
                    if (isCanFlyByBattary) "????????????:${battery}" else "????????????30%")

        })
        RxBus.INSTANCE.doStickySubscribe(StorageState::class.java) { storageState ->
            val isCanFlyByStorage = if (storageState.isFull || storageState.isReadOnly) {
                false
            } else {
                storageState.totalSpaceInMB >= 1024
            }
            STORAGE.value = TakeOffInfoEntity(if (isCanFlyByStorage) DetectionViewType.OK else DetectionViewType.WARRAY,
                    if (isCanFlyByStorage) "????????????:${storageState.remainingSpaceInMB/ 1024}"  else "????????????1g")
        }

        RxBus.INSTANCE.doStickySubscribe(HardwareState::class.java) { hardwareState ->
            val isCanFlyByHardware = hardwareState.flightModeSwitch == HardwareState.FlightModeSwitch.POSITION_THREE
            HARDWARE.value = TakeOffInfoEntity(if (isCanFlyByHardware) DetectionViewType.OK else DetectionViewType.WARRAY,
                    if (isCanFlyByHardware) "????????????:${hardwareState.flightModeSwitch}" else "???????????????p???")

        }
//        RxBus.INSTANCE.doDefaultSubscribe(DJIAirLinkEven::class.java, Consumer { djiAirLinkEven ->
//            if (djiAirLinkEven.type == DJIAirLinkEven.AirLinkType.DOWNLOAD) {
//                return@Consumer
//            }
//            isCanFlyByAirLink = djiAirLinkEven.signal > 70
//            iView.changeAirLink(if (isCanFlyByAirLink) DetectionViewType.OK else DetectionViewType.WARRAY,
//                    djiAirLinkEven.signal)
//        })
        RxBus.INSTANCE.doDefaultSubscribe(FlightControllerState::class.java) { flightControllerState ->

            goHomeLocation = flightControllerState.homeLocation
            currentLocation = flightControllerState.aircraftLocation
            val isCanFlyByGPS = flightControllerState.satelliteCount >= 7
//            if (null != iView) {
//               val  isCanFlyByHomePoint = if (null != flightControllerState.homeLocation) {
//                    iView.changeHomePoint(DetectionViewType.OK, "?????????????????????")
//                    true
//                } else {
//                    iView.changeHomePoint(DetectionViewType.WARRAY, "??????????????????")
//                    false
//                }
//                isCanFlyByDistanc = if (null == flightControllerState.aircraftLocation) {
//                    iView.changeDistance(DetectionViewType.WARRAY, "????????????????????????")
//                    false
//                } else {
//                    val dis = is2000Distance(flightControllerState.aircraftLocation)
//                    if (dis) {
//                        iView.changeDistance(DetectionViewType.OK, "?????????????????????")
//                        true
//                    } else {
//                        iView.changeDistance(DetectionViewType.WARRAY, "?????????????????????")
//                        false
//                    }
//                }
//                iView.changeGps(if (isCanFlyByGPS) DetectionViewType.OK else DetectionViewType.WARRAY, "???????????????:" + flightControllerState.satelliteCount)
//            }
        }

        //????????????????????????
        RxBus.INSTANCE.doDefaultSubscribe(WaypointMissionUploadEvent::class.java, Consumer { waypointMissionUploadEvent ->
//
//            val currentState = waypointMissionUploadEvent.currentState
//            val waypointUploadProgress = waypointMissionUploadEvent.progress
//            if (currentState === WaypointMissionState.UPLOADING) {
//                val info = String.format("?????????????????????:%s/%s", waypointUploadProgress.uploadedWaypointIndex, waypointUploadProgress.totalWaypointCount)
//                iView.changeMission(DetectionViewType.NORMAL, info)
//            } else if (currentState === WaypointMissionState.READY_TO_EXECUTE) {
//                isCanFlyByMission = true
//                iView.changeMission(DetectionViewType.OK, "????????????")
//            } else {
//                isCanFlyByMission = false
//                iView.changeMission(DetectionViewType.WARRAY, currentState.toString())
//            }
        })
    }

    private fun is2000Distance(location: LocationCoordinate3D): Boolean {
//        val airLatlng = LatLng(location.latitude, location.longitude)
//        val tasks: List<LatLng> = mBaseMissionInfoEntity.getTasks()
//        if (tasks == null || tasks.size < 1) {
//            return false
//        }
//        for (latLng in tasks) {
//
////            if (ZAMapUtil.calDistance(latLng, airLatlng) > 2000) {
////                ToastUtil.showToast("??????????????????????????????????????????????????????????????????")
////                return false
////            }
//        }
        return true
    }

    /**
     * ????????????
     */
    private fun setCamera() {
//        setCameraModle(mBaseSettingEntity.getFlySetting().getmTaskCameraType())
        setCameraFormat()
        setPhotoAspectRatio()
        judgmentCamera()
        DJICameraHelper.instance?.SetCameraAuto()
    }

    /**
     * ??????????????????
     */
    private fun setCameraModle(cameraModle: SettingsDefinitions.CameraMode) {
    }

    /**
     * ????????????????????????
     */
    private fun setCameraFormat() {
//        addDisposable(
//                Observable.timer(1, TimeUnit.SECONDS)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe {
//                            DJICameraHelper.getInstance().setPhotoFileFormat(mBaseSettingEntity
//                                    .getCameraSetting().getmPhotoFileFormat()) { result2 ->
//                                if (null == result2) {
//                                    photoFormat = true
//                                    getView().changeCamera(DetectionViewType.NORMAL, "???????????????????????????????????????????????????")
//                                } else {
//                                    photoFormat = false
//                                    getView().changeCamera(DetectionViewType.WARRAY, "?????????????????????" + result2.toString())
//                                }
//                            }
//                        })
    }

    /**
     * ?????????????????????????????????
     */
    private fun setPhotoAspectRatio() {
//        addDisposable(
//                Observable.timer(2, TimeUnit.SECONDS)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe {
//                            DJICameraHelper.getInstance().setPhotoFileFormat(mBaseSettingEntity
//                                    .getCameraSetting().getmPhotoFileFormat(), ICommonCallBackWith<DJIError?> { result2 ->
//                                if (null == result2) {
//                                    photoAspectRatio = true
//                                    getView().changeCamera(DetectionViewType.NORMAL, "????????????????????????")
//                                } else {
//                                    photoAspectRatio = false
//                                    getView().changeCamera(DetectionViewType.WARRAY, "?????????????????????$result2")
//                                }
//                            })
//                        })
    }

    private fun judgmentCamera() {
//        addDisposable(
//                Observable.timer(3, TimeUnit.SECONDS)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe {
//                            isCanFlyByCamera = cameraModel && photoFormat && photoAspectRatio
//                            //                                getView().changeCamera(DetectionViewType.NORMAL, cameraModle + "2" + photoFormat + "3" + photoAspectRatio);
//                            if (isCanFlyByCamera) {
//                                getView().changeCamera(DetectionViewType.OK, "????????????????????????")
//                            }
//                        })
    }

    fun isCanFly(): Boolean {return false
//        return (isCanFlyByHomePoint and isCanFlyByConnect and isCanFlyByGPS
//                and isCanFlyByStorage and isCanFlyByCamera and isCanFlyByHardware
//                and isCanFlyByBattary and isCanFlyByMission and isCanFlyByDistanc)
    }


    fun addDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    fun detachView() {
        mCompositeDisposable.clear()

    }

}