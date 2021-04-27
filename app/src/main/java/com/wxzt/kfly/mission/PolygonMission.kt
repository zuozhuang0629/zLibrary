package com.wxzt.kfly.mission

import com.amap.api.maps.model.LatLng
import com.orhanobut.logger.Logger
import com.wxzt.kfly.db.entity.LocationTaskEntity
import com.wxzt.kfly.ustils.ZAMapUtil

import dji.common.camera.SettingsDefinitions
import dji.common.mission.waypoint.*

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2020/1/22 13:38
 * Description: 多边形任务
 * History:
 */
class PolygonMission : IDJIMission {


    override fun getMission(locationTaskEntity: LocationTaskEntity, doneProgress: Int): WaypointMission {
        val planningSettingEntity = locationTaskEntity.flySettingEntity

        val builder = WaypointMission.Builder()
        builder.autoFlightSpeed(planningSettingEntity.takeOffSpeed.toFloat())
                .maxFlightSpeed(WaypointMission.MAX_FLIGHT_SPEED)
                .finishedAction(WaypointMissionFinishedAction.GO_HOME)
                .headingMode(WaypointMissionHeadingMode.AUTO)
                .gotoFirstWaypointMode(WaypointMissionGotoWaypointMode.SAFELY)
                .setExitMissionOnRCSignalLostEnabled(false)
                .flightPathMode(WaypointMissionFlightPathMode.NORMAL)
        val allWayPoint: List<LatLng> = locationTaskEntity.wayPointList
        val size: Int = allWayPoint.size
        val done = Math.max(0, planningSettingEntity.taskComplete)
        val altitude: Int = planningSettingEntity.flyHeight
        val interval = getInterval(planningSettingEntity.verticalOverlap, planningSettingEntity.taskSpeed.toFloat(), altitude)
        val angle: Int = 90
        for (i in done until size) {
            val latLng: LatLng = ZAMapUtil.gcj_To_Gps84(allWayPoint[i].longitude,
                    allWayPoint[i].latitude)
            val point = Waypoint(latLng.latitude, latLng.longitude, altitude.toFloat())
            point.speed = planningSettingEntity.taskSpeed.toFloat()
            point.addAction(WaypointAction(WaypointActionType.GIMBAL_PITCH, -angle))
            point.shootPhotoTimeInterval = interval.toFloat()
            builder.addWaypoint(point)
        }
        return builder.build()
    }

    private fun getInterval(rateF: Float, speedF: Float, altitudeF: Int): Int {
        val angle = 84.8 / 2.0
        val time = Math.tan(Math.PI / 180.0 * angle) * altitudeF.toDouble() * (1.0 - rateF.toDouble()) / speedF.toDouble()
        val interval = Math.max(5, Math.round(time)).toInt()
        return interval
    }

}