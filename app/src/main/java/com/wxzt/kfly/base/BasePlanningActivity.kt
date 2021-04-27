package com.wxzt.kfly.base


import androidx.viewbinding.ViewBinding
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.blankj.utilcode.util.ToastUtils
import com.wxzt.kfly.appViewModel
import com.wxzt.kfly.dji.DJICameraHelper
import com.wxzt.kfly.dji.DJIDeviceHelper
import com.wxzt.kfly.dji.DJILiveHelper
import com.wxzt.kfly.enums.DjiProductStatus
import com.wxzt.kfly.enums.LiveStatusEnum
import com.wxzt.lib_common.event.DJIAirLinkEven
import com.wxzt.lib_common.rx.RxBus
import com.wxzt.lib_common.utils.CameraShoudPlayUtil
import dji.common.battery.BatteryState
import dji.common.camera.StorageState
import dji.common.camera.SystemState
import dji.common.error.DJIError
import dji.common.flightcontroller.FlightControllerState
import dji.common.gimbal.GimbalState
import dji.common.mission.waypoint.WaypointMissionDownloadEvent
import dji.common.mission.waypoint.WaypointMissionExecutionEvent
import dji.common.mission.waypoint.WaypointMissionUploadEvent
import dji.common.remotecontroller.HardwareState
import dji.sdk.media.MediaFile
import dji.sdk.mission.MissionControl
import dji.sdk.mission.waypoint.WaypointMissionOperatorListener
import dji.sdk.products.HandHeld
import dji.sdk.sdkmanager.LiveStreamManager

/**
 *  author : zuoz
 *  date : 2021/3/29 11:10
 *  description : 航线规划base
 */
abstract class BasePlanningActivity<VB : ViewBinding> : BaseActivity<VB>() {
    private val liveListener: LiveStreamManager.OnLiveChangeListener? = null

    override fun initListener() {
        appViewModel.djiRegisterStatus.observe(this) {
            setProductStatusListener(it)
        }

        //dji相关监听
        registerCameraListener()
        registerBatteryListener()
        registerLiveListener()
        registerHardwareListener()
        registerMissionListener()
        registerAirLinkStateListener()
        registerGimbalStateListener()
        registerFlightControllerStateListener()
    }


    /**
     * 注册相机监听
     */
    private fun registerCameraListener() {
        val camera = DJICameraHelper.instance?.camera ?: return

        //相机状态
        camera.setSystemStateCallback { systemState -> setCameraListener(systemState) }

        //内存
        camera.setStorageStateCallBack { storageState -> setStorageStateListener(storageState) }

        //拍照文件
        camera.setMediaFileCallback { mediaFile ->
            CameraShoudPlayUtil.Companion.instance.play()
            setMediaFileListener(mediaFile)
        }
    }

    /**
     * battery  listener
     */
    private fun registerBatteryListener() {
        val baseProduct = DJIDeviceHelper.instance.productInstance ?: return
        val battery = baseProduct.battery ?: return
        battery.setStateCallback { batteryState -> setBatteryStateListener(batteryState) }
    }

    /**
     * 设置档位监听
     */
    private fun registerHardwareListener() {
        val aircraft = DJIDeviceHelper.instance.aircraftInstance ?: return
        aircraft.remoteController.setHardwareStateCallback { hardwareState ->
            setHardwareStateListener(hardwareState)
        }
    }

    /**
     * 设置直播监听
     *
     */
    private fun registerLiveListener() {
        DJILiveHelper.instacne.setLiveListener {
            if (it == -1) {
                setLiveStatusListener(LiveStatusEnum.LIVE_FAIL)
            } else if (it == 0) {
                setLiveStatusListener(LiveStatusEnum.LIVE_SUCCESS)
            } else {
                setLiveStatusListener(LiveStatusEnum.LIVE_CLOSE)
            }
        }
    }

    /**
     * 注册任务监听
     */
    private fun registerMissionListener() {
        val missionOperator = MissionControl.getInstance().waypointMissionOperator
                ?: return
        missionOperator.addListener(object : WaypointMissionOperatorListener {
            override fun onDownloadUpdate(waypointMissionDownloadEvent: WaypointMissionDownloadEvent) {}
            override fun onUploadUpdate(waypointMissionUploadEvent: WaypointMissionUploadEvent) {
                setMissionUploadUpdateListener(waypointMissionUploadEvent)
            }

            override fun onExecutionUpdate(waypointMissionExecutionEvent: WaypointMissionExecutionEvent) {
                setExecutionUpdateListener(waypointMissionExecutionEvent)
            }

            override fun onExecutionStart() {
                setExecutionStartListener()
            }

            override fun onExecutionFinish(djiError: DJIError?) {
                djiError?.let {
                    setExecutionFinishListener()
                }

            }
        })
    }


    /**
     * 注册遥控信息号监听
     */
    private fun registerAirLinkStateListener() {
        val baseProduct = DJIDeviceHelper.instance.productInstance ?: return
        val airLink = baseProduct.airLink ?: return


//        //控制无人机信息号
//        airLink.setUplinkSignalQualityCallback { i -> RxBus.INSTANCE.post(DJIAirLinkEven(DJIAirLinkEven.AirLinkType.UPLOAD, i)) }
//
//        //图传信号
//        airLink.setDownlinkSignalQualityCallback { i -> RxBus.INSTANCE.post(DJIAirLinkEven(DJIAirLinkEven.AirLinkType.DOWNLOAD, i)) }
    }

    /**
     * 注册云台状态号监听
     */
    private fun registerGimbalStateListener() {
        val baseProduct = DJIDeviceHelper.instance.productInstance ?: return
        val gimbal = baseProduct.gimbal ?: return

        //控制无人机信息号
        gimbal.setStateCallback { gimbalState -> setGimbalStateListener(gimbalState) }
    }


    /**
     * 注册飞行状态监听
     */
    private fun registerFlightControllerStateListener() {
        val aircraft = DJIDeviceHelper.instance.aircraftInstance ?: return
        aircraft.flightController.setStateCallback { flightControllerState ->
            setFlightControllerStateListener(flightControllerState)
            val isNeedLanding = flightControllerState.isLandingConfirmationNeeded
            if (isNeedLanding) {
                DJIDeviceHelper.instance.flightController?.confirmLanding {

                }
            }
        }
    }

    /**
     * 移除监听
     */
    private fun unRegisterListener() {
        //直播
        DJILiveHelper.instacne.removeLiveListener(liveListener)
        //相机
    }

    private fun registerHandHeldListener() {
        val handHeld = DJIDeviceHelper.instance.productInstance as HandHeld
        val handheldController = handHeld.handHeldController ?: return
        handheldController.setHardwareStateCallback { hardwareState ->

        }
    }

    /**
     * 移动相机
     *
     * @param map
     * @param latLng
     */
    protected fun moveCamera(map: AMap, latLng: LatLng?) {
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(CameraPosition(
                latLng, 16f, 0f, 0f))
        map.moveCamera(cameraUpdate)
    }


    //region
    /**
     * 相机回调
     *
     * @param systemState
     */
    protected open fun setCameraListener(systemState: SystemState) {

    }

    /**
     * 内存回调
     *
     * @param storageState
     */
    protected open fun setStorageStateListener(storageState: StorageState) {


    }

    /**
     * 拍照文件
     *
     * @param mediaFile
     */
    protected open fun setMediaFileListener(mediaFile: MediaFile) {

    }

    /**
     * 电池状态
     *
     * @param batteryState
     */
    protected open fun setBatteryStateListener(batteryState: BatteryState) {

    }


    /**
     * 设置档位变化
     *
     * @param hardwareState
     */
    protected open fun setHardwareStateListener(hardwareState: HardwareState) {

    }

    /**
     * 无人机状态监听
     *
     * @param flightControllerState
     */
    protected open fun setFlightControllerStateListener(flightControllerState: FlightControllerState) {

    }


    /**
     *  设置任务上传
     *
     * @param waypointMissionUploadEvent
     */
    protected open fun setMissionUploadUpdateListener(waypointMissionUploadEvent: WaypointMissionUploadEvent) {

    }

    /**
     *  设置执行
     *
     * @param waypointMissionUploadEvent
     */
    protected open fun setExecutionUpdateListener(waypointMissionExecutionEvent: WaypointMissionExecutionEvent) {

    }

    /**
     *  任务开始
     *
     */
    protected open fun setExecutionStartListener() {

    }

    protected open fun setExecutionFinishListener() {

    }

    /**
     *  设置万向节状态监听
     *
     */
    protected open fun setGimbalStateListener(gimbalState: GimbalState) {

    }

    /**
     * 设置dji无人机直播监听
     *
     * @param liveStatusEnum
     */
    protected open fun setLiveStatusListener(liveStatusEnum: LiveStatusEnum) {

    }

    protected open fun setProductStatusListener(djiProductStatus: DjiProductStatus) {


    }

    //endregion
}