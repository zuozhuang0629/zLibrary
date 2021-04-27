package com.wxzt.kfly.ui.dialog

import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.amap.api.maps.model.LatLng
import com.blankj.utilcode.util.ToastUtils
import com.wxzt.kfly.R
import com.wxzt.kfly.appViewModel
import com.wxzt.kfly.databinding.DialogTaskOffCheckBinding
import com.wxzt.kfly.db.entity.LocationTaskEntity
import com.wxzt.kfly.dji.DJICameraHelper
import com.wxzt.kfly.dji.DJIDeviceHelper
import com.wxzt.kfly.dji.DJIWayMissionHelper
import com.wxzt.kfly.dji.ICommonCallBack
import com.wxzt.kfly.enums.DjiProductStatus
import com.wxzt.kfly.enums.TaskModelEnum
import com.wxzt.kfly.mission.FixMission
import com.wxzt.kfly.mission.IDJIMission
import com.wxzt.kfly.mission.PolygonMission
import com.wxzt.lib_common.enums.DetectionViewType
import com.wyzt.lib_common.base.dialog.BaseVBDialog
import com.wxzt.lib_common.ext.clickWithDuration
import com.wxzt.lib_common.ext.visibility
import dji.common.camera.SettingsDefinitions
import dji.common.error.DJIError
import dji.common.mission.waypoint.WaypointMission
import dji.common.mission.waypoint.WaypointMissionState
import dji.common.remotecontroller.HardwareState
import dji.common.util.CommonCallbacks
import dji.keysdk.BatteryKey
import dji.keysdk.KeyManager
import dji.sdk.mission.MissionControl
import dji.ux.beta.core.base.DJISDKModel
import dji.ux.beta.core.communication.ObservableInMemoryKeyedStore
import dji.ux.beta.core.widget.battery.BatteryWidgetModel
import kotlin.math.max

/**
 *  author      : zuoz
 *  date        : 2021/4/13 16:16
 *  description :飞行检测dialog
 */
class TaskOffCheckDialog(private val locationTaskEntity: LocationTaskEntity,
                         val block: () -> Unit = {}) : BaseVBDialog<DialogTaskOffCheckBinding>() {

    //region Fields
    private val widgetModel by lazy {
        TaskOffCheckMV(
                DJISDKModel.getInstance(),
                ObservableInMemoryKeyedStore.getInstance())
    }

    private var isFlys = arrayListOf<Boolean>(false, false, false, false, false, false, false)
    private val mDjiMission: IDJIMission by lazy {
        when (locationTaskEntity.flySettingEntity.taskModel) {
            TaskModelEnum.POLYGON -> PolygonMission()
            TaskModelEnum.OBLIQUE -> PolygonMission()
            TaskModelEnum.FIX -> FixMission()
            TaskModelEnum.PANORAMIC -> PolygonMission()
        }
    }


    override fun getLayoutId(): Int = R.layout.dialog_task_off_check

    override fun setDialogGravity(): Int = Gravity.CENTER

    override fun getViewBinding(): DialogTaskOffCheckBinding = DialogTaskOffCheckBinding.inflate(layoutInflater)

    override fun setDialogSize(wlp: WindowManager.LayoutParams, window: Window) {
        val wm = context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)

        wlp.width = (dm.widthPixels * 0.6).toInt()
        wlp.height = (dm.heightPixels * 0.7).toInt()

        window.attributes = wlp
    }

    override fun getAnim(): Int = R.style.dialog_default_animate

    override fun getIsOnTouchOutside(): Boolean = false

    override fun initView() {
        mViewBinding.dialogBtnCancel.setOnClickListener {
            this.dismiss()
        }

        widgetModel.setup()
//        initCheck()
//        initListener()

//        loadTask()
//        mViewBinding.dialogBtnStart.clickWithDuration {
//            this.dismiss()
//            val waypointMissionOperator = MissionControl.getInstance().waypointMissionOperator
//            waypointMissionOperator?.let {
//                it.startMission { djierror ->
//                    djierror?.let {
//                        ToastUtils.showLong("执行任务错误:${it.description}")
//                    } ?: let {
//                        block?.invoke()
//                    }
//                }
//            }
//        }
    }

    private fun loadTask() {

        val tasks: List<LatLng> = locationTaskEntity.wayPointList

        if (tasks.isEmpty() || tasks.size < 2) {
            mViewBinding.missionInfo.text = "加载任务失败:任务点至少2个"
            return
        }

        val waypointMission: WaypointMission = mDjiMission.getMission(locationTaskEntity,
                max(locationTaskEntity.flySettingEntity.taskComplete, 0))

        val waypointMissionOperator = MissionControl.getInstance().waypointMissionOperator
        val djiError: DJIError? = waypointMissionOperator.loadMission(waypointMission)
        mViewBinding.missionInfo.text = "航线任务正在上传中..."

        if (null == djiError) {
            if (WaypointMissionState.READY_TO_RETRY_UPLOAD == waypointMissionOperator.currentState
                    || WaypointMissionState.READY_TO_UPLOAD == waypointMissionOperator.currentState) {
                waypointMissionOperator.uploadMission {
                    mViewBinding.missionInfo.text = "航线任务正在上传中..."
                }
            } else {
                mViewBinding.missionInfo.text = "加载任务失败"
            }

        } else {
            mViewBinding.missionInfo.text = "加载任务失败$djiError"
        }
    }


    private fun initCheck() {

        widgetModel.check.observe(viewLifecycleOwner) {
            if (it.isCoConnection) {
                mViewBinding.libCommonDvIsConnect.setState(DetectionViewType.OK, "无人机已连接")
                isFlys[0] = true

                if (it.battery >= 30) {
                    isFlys[1] = true
                    mViewBinding.libCommonDvBattery.setState(DetectionViewType.WARRAY, "当前电量:${it.battery}%")
                } else {
                    isFlys[1] = false
                    mViewBinding.libCommonDvBattery.setState(DetectionViewType.WARRAY, "无人机电量必须大于30%")
                }

                checkCamera(it.photoForMat, it.shootPhotoModed)

                if (it.satelliteCount > 7) {
                    isFlys[3] = true
                    mViewBinding.libCommonDvGps.setState(DetectionViewType.OK, "当前卫星:${it.satelliteCount}")
                } else {
                    isFlys[3] = false
                    mViewBinding.libCommonDvGps.setState(DetectionViewType.WARRAY, "卫星必须大于7颗")
                }

                if (it.sdStorage >= 1024 * 2) {
                    isFlys[4] = true
                    mViewBinding.libCommonDvStorage.setState(DetectionViewType.OK, "当前内存:${it.sdStorage / 1024}")
                } else {
                    isFlys[4] = false
                    mViewBinding.libCommonDvStorage.setState(DetectionViewType.OK, "可用内存必须大于2G")

                }

            } else {
                isFlys = arrayListOf<Boolean>(false, false, false, false, false, false)
                mViewBinding.libCommonDvIsConnect.setState(DetectionViewType.WARRAY, "无人机未连接")
                mViewBinding.libCommonDvCamera.setState(DetectionViewType.WARRAY, "无人机未连接")
                mViewBinding.libCommonDvBattery.setState(DetectionViewType.WARRAY, "无人机未连接")
                mViewBinding.libCommonDvGps.setState(DetectionViewType.WARRAY, "无人机未连接")
                mViewBinding.libCommonDvStorage.setState(DetectionViewType.WARRAY, "无人机未连接")
                mViewBinding.libCommonDvHardware.setState(DetectionViewType.WARRAY, "无人机未连接")
            }

            isShowFly()
        }




        DJICameraHelper.instance?.camera?.let {
            it.getMode(object : CommonCallbacks.CompletionCallbackWith<SettingsDefinitions.CameraMode> {
                override fun onSuccess(p0: SettingsDefinitions.CameraMode?) {
                    p0?.let { mdoel ->
                        if (mdoel == SettingsDefinitions.CameraMode.SHOOT_PHOTO) {

                            isShowFly()
                            mViewBinding.libCommonDvCamera.setState(DetectionViewType.OK, "相机模式正确(拍照)")
                        } else {
                            DJICameraHelper.instance?.switchCameraModel(SettingsDefinitions.CameraMode.SHOOT_PHOTO,
                                    object : ICommonCallBack.ICommonResultCallBackWith<String> {
                                        override fun onSuccess(result: String?) {

                                        }

                                        override fun onFailure(djiError: DJIError?) {

                                        }

                                    })
                        }
                    }

                }

                override fun onFailure(djiError: DJIError?) {
                }

            })
        }

    }

    private fun initListener() {


        appViewModel.djiWaypointMissionUploadEvent.observe(this) {
            val currentState = it.currentState
            val waypointUploadProgress = it.progress

            if (currentState === WaypointMissionState.UPLOADING) {

                val progress = waypointUploadProgress!!.uploadedWaypointIndex * 100 / waypointUploadProgress!!.totalWaypointCount

                mViewBinding.missionProgress.progress = progress
                mViewBinding.missionPercentage.text = "${progress}%"

            } else if (currentState === WaypointMissionState.READY_TO_EXECUTE) {
                isFlys[6] = true
                mViewBinding.missionInfo.text = "上传成功"
                mViewBinding.missionProgress.progress = 100
                mViewBinding.missionPercentage.text = "100%"

            } else {
                isFlys[6] = false
                mViewBinding.missionInfo.text = "上传失败，请重新上传"
                mViewBinding.missionProgress.progress = 0
                mViewBinding.missionPercentage.text = "0%"
            }
        }

        mViewBinding.dialogBtnStart.clickWithDuration {
            val waypointMissionOperator = MissionControl.getInstance().waypointMissionOperator
            waypointMissionOperator.startMission { djierror ->
                djierror?.let {
                    ToastUtils.showLong("执行任务错误:${it.description}")
                } ?: let {
                    this.dismiss()
                }
            }
        }
    }


    private fun checkCamera(photoForMat: SettingsDefinitions.PhotoFileFormat,
                            shootPhotoModed: SettingsDefinitions.ShootPhotoMode) {
        when (photoForMat) {

            SettingsDefinitions.PhotoFileFormat.JPEG,
            SettingsDefinitions.PhotoFileFormat.RADIOMETRIC_JPEG_LOW,
            SettingsDefinitions.PhotoFileFormat.RADIOMETRIC_JPEG_HIGH,
            SettingsDefinitions.PhotoFileFormat.RADIOMETRIC_JPEG -> {
                mViewBinding.libCommonDvCamera.setState(DetectionViewType.OK, "图片格式:jpg")
            }

            else -> {
                mViewBinding.libCommonDvCamera.setState(DetectionViewType.WARRAY, "图片格式必须未jpg")
            }
        }

        if (mViewBinding.libCommonDvCamera.currentState == DetectionViewType.WARRAY) {
            isFlys[2] = false
            return
        }

        when (shootPhotoModed) {
            SettingsDefinitions.ShootPhotoMode.SINGLE -> {
                mViewBinding.libCommonDvCamera.setState(DetectionViewType.OK, "拍照模式:单张拍照")
                isFlys[2] = true
            }
            else -> {
                isFlys[2] = false
                mViewBinding.libCommonDvCamera.setState(DetectionViewType.OK, "图片格式必须是单张拍照")
            }
        }
    }

    fun isShowFly() {
        var isShow = true
        for (check in isFlys) {
            if (!check) {
                isShow = false
                return
            }
        }

        mViewBinding.dialogBtnStart.visibility(isShow)

    }
}