package com.wxzt.kfly.viewmodel

import androidx.lifecycle.MutableLiveData

import com.wxzt.kfly.db.entity.LocationTaskEntity
import com.wxzt.kfly.dji.DJIEvent
import com.wxzt.kfly.entitys.UserInfo
import com.wxzt.kfly.enums.DjiProductStatus
import com.wxzt.kfly.network.api.Api
import com.wxzt.kfly.ustils.CacheUtil
import com.wxzt.lib_common.viewmodel.BaseViewModel
import dji.common.battery.BatteryState
import dji.common.camera.StorageState
import dji.common.camera.SystemState
import dji.common.flightcontroller.FlightControllerState
import dji.common.gimbal.GimbalState
import dji.common.mission.waypoint.WaypointMissionUploadEvent
import dji.common.product.Model
import dji.common.remotecontroller.HardwareState

/**
 *  author : zuoz
 *  date : 2021/4/1 17:07
 *  description :
 */
class AppViewModel : BaseViewModel() {
    //App的账户信息
    var userInfo = MutableLiveData<UserInfo>()

    //当前执行的任务信息
    var taskInfo = MutableLiveData<LocationTaskEntity>()

    //无人机的链接状态
    val djiRegisterStatus = DJIEvent<DjiProductStatus>()

    //设置的网络
    val httpConfigInfo = MutableLiveData<String>()

//    val djiProduct = DJIEvent<Model>()
//    val djiBatteryStatus = DJIEvent<BatteryState>()
//    val djiCameraStatus = DJIEvent<SystemState>()
//    val djiStorageStatus = DJIEvent<StorageState>()
//    val djiHardwareStatus = DJIEvent<HardwareState>()
//    val djiFlightControllerState = DJIEvent<FlightControllerState>()
//    val djiGimbalState = DJIEvent<GimbalState>()
    val djiWaypointMissionUploadEvent = DJIEvent<WaypointMissionUploadEvent>()


    init {
        //默认值保存的账户信息，没有登陆过则为null
        userInfo.value = CacheUtil.getUserInfo()
        val http = CacheUtil.getHttpConfig()
        http?.let {
            httpConfigInfo.value = it
        } ?: let {
            httpConfigInfo.value = Api.BASE_TEST_NAME
        }
    }
}