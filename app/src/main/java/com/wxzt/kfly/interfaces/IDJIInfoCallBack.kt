package com.wxzt.kfly.interfaces

import dji.common.battery.BatteryState
import dji.common.camera.StorageState
import dji.common.camera.SystemState
import dji.common.flightcontroller.FlightControllerState
import dji.common.gimbal.GimbalState
import dji.common.mission.waypoint.WaypointMissionExecutionEvent
import dji.common.mission.waypoint.WaypointMissionUploadEvent
import dji.common.remotecontroller.GPSData
import dji.common.remotecontroller.HardwareState
import dji.sdk.media.MediaFile

/**
 *  author      : zuoz
 *  date        : 2021/4/13 17:10
 *  description :
 */
interface IDJIInfoCallBack {
    /**
     * 相机回调
     *
     * @param systemState
     */
    fun setCameraListener(systemState: SystemState) {

    }

    /**
     * 内存回调
     *
     * @param storageState
     */
    fun setStorageStateListener(storageState: StorageState) {


    }

    /**
     * 拍照文件
     *
     * @param mediaFile
     */
    fun setMediaFileListener(mediaFile: MediaFile) {

    }

    /**
     * 电池状态
     *
     * @param batteryState
     */
    fun setBatteryStateListener(batteryState: BatteryState) {

    }

    /**
     *  设置卫星状态
     *
     * @param gpsData
     */
    fun setRemoteControllerGpsListener(gpsData: GPSData) {

    }

    /**
     * 设置档位变化
     *
     * @param hardwareState
     */
    fun setHardwareStateListener(hardwareState: HardwareState) {

    }

    /**
     * 无人机状态监听
     *
     * @param flightControllerState
     */
    fun setFlightControllerStateListener(flightControllerState: FlightControllerState) {

    }


    /**
     *  设置任务上传
     *
     * @param waypointMissionUploadEvent
     */
    fun setMissionUploadUpdateListener(waypointMissionUploadEvent: WaypointMissionUploadEvent) {

    }

    /**
     *  设置执行
     *
     * @param waypointMissionUploadEvent
     */
    fun setExecutionUpdateListener(WaypointMissionExecutionEvent: WaypointMissionExecutionEvent) {

    }

    /**
     *  任务开始
     *
     */
    fun setExecutionStartListener() {

    }

    fun setExecutionFinishListener() {

    }

    /**
     *  设置万向节状态监听
     *
     */
    fun setGimbalStateListener(gimbalState: GimbalState) {

    }
}