package com.wxzt.kfly.mission

import com.amap.api.maps.model.LatLng
import com.wxzt.kfly.db.entity.LocationTaskEntity
import com.wxzt.kfly.ustils.ZAMapUtil
import dji.common.camera.SettingsDefinitions
import dji.common.mission.waypoint.*

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/12/6 14:48
 * Description:
 * History:
 */
class FixMission() : IDJIMission {

    override fun getMission(locationTaskEntity: LocationTaskEntity,
                            doneProgress: Int): WaypointMission {

        val speed: Int = locationTaskEntity.flySettingEntity.taskSpeed
        //初始化参数
        val builder = WaypointMission.Builder()
        builder.maxFlightSpeed(15f)
        builder.autoFlightSpeed(speed.toFloat())
        builder.headingMode(WaypointMissionHeadingMode.AUTO)
        builder.finishedAction(WaypointMissionFinishedAction.GO_HOME)
        builder.flightPathMode(WaypointMissionFlightPathMode.NORMAL)
        builder.gotoFirstWaypointMode(WaypointMissionGotoWaypointMode.SAFELY)
        val taskList: List<LatLng> = locationTaskEntity.wayPointList
        for (index in doneProgress until taskList.size) {
            val latLng: LatLng = ZAMapUtil.gcj_To_Gps84(taskList[index].longitude,
                    taskList[index].latitude)
            val point = Waypoint(latLng.latitude, latLng.longitude, locationTaskEntity.flySettingEntity.flyHeight.toFloat())
//            point.addAction(WaypointAction(WaypointActionType.GIMBAL_PITCH,
//                    -fixFlySettingEntity.getFlySetting().getCameraAngle()))


            builder.addWaypoint(point)
        }
        return builder.build()
    }


}