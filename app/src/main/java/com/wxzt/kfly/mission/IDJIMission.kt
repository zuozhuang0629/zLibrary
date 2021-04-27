package com.wxzt.kfly.mission

import com.wxzt.kfly.db.entity.LocationTaskEntity
import dji.common.mission.waypoint.WaypointMission

/**
 *  author      : zuoz
 *  date        : 2021/4/15 8:50
 *  description :大疆任务
 */
interface IDJIMission {
    fun getMission(locationTaskEntity: LocationTaskEntity, doneProgress: Int): WaypointMission
}