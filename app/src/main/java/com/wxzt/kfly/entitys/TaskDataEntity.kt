package com.wxzt.kfly.entitys

import com.amap.api.maps.model.LatLng
import com.wxzt.kfly.db.entity.FlySettingEntity
import com.wxzt.kfly.db.entity.LocationTaskEntity
import com.wxzt.kfly.enums.TaskModelEnum
import com.wxzt.kfly.enums.TaskStatusEnum
import com.wxzt.kfly.enums.TaskTypeEnum
import org.json.JSONArray

import java.util.*

/**
 *  author : zuoz
 *  date : 2021/4/6 10:13
 *  description :
 */
data class TaskDataEntity(
        val body: Body?,
        val head: Head
)

data class Body(
        val resultData: List<TaskData>
)

data class Head(
        val rtnCode: String,
        val rtnMessage: String
)

data class TaskData(
        val userName: String,
        val coordinate: String,//控制点
        val createTime: String,
        val flyHeight: Int,
        val flyTaskName: String,
        val headingOverlap: Float,
        val id: String,
        val lineOverlap: Float,
        val rotatePitch: Float,
        val speed: Int,
        val wayPointList: List<WayPoint>//任务点
) {
    fun getLocationData(userName: String, taskModel: TaskModelEnum, createTime: Date): LocationTaskEntity {
        val newWayPoints = arrayListOf<LatLng>()
        for (wayPoint in wayPointList) {
            newWayPoints.add(LatLng(wayPoint.latitude, wayPoint.longitude))
        }

        val taskControl = arrayListOf<LatLng>()
        val controls = (JSONArray(coordinate)[0] as JSONArray)
        for (control in 0 until controls.length()) {
            val temp = (controls[control] as JSONArray)
            val lng = temp.get(0) as Double
            val lat = temp.get(1) as Double
            taskControl.add(LatLng(lat, lng))

        }

        val flySettingEntity = FlySettingEntity(taskModel, speed, 10,
                flyHeight, lineOverlap, headingOverlap, 0)

        return LocationTaskEntity(
                id, userName, flyTaskName,
                taskStatus = TaskStatusEnum.NO_EXECUTE,
                createTime = createTime, flySettingEntity = flySettingEntity,
                taskControl = taskControl, wayPointList = newWayPoints,
                0, "",TaskTypeEnum.SERVICE)
    }
}

data class WayPoint(
        val id: String,
        val latitude: Double,
        val longitude: Double,
        val order: Int
)