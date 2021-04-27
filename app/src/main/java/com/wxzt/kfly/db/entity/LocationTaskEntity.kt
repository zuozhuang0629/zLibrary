package com.wxzt.kfly.db.entity

import androidx.room.*
import com.amap.api.maps.model.LatLng
import com.wxzt.kfly.enums.TaskStatusEnum
import com.wxzt.kfly.enums.TaskTypeEnum

import java.util.*

/**
 * @Author:          zuoz
 * @CreateDate:     2021/1/28 17:46
 * @Description:    任务
 */
@Entity(tableName = "task")
data class LocationTaskEntity(
        @PrimaryKey val missionId: String,//任务id
        @ColumnInfo(name = "user_name") var userName: String,//用户名
        @ColumnInfo(name = "task_name") var missionName: String,//任务名
        @ColumnInfo(name = "task_status") var taskStatus: TaskStatusEnum,//任务完成状态
        @ColumnInfo(name = "create_time") var createTime: Date,//创建时间
        @Embedded var flySettingEntity: FlySettingEntity,//飞行设置参数
        @ColumnInfo(name = "task_control") var taskControl: List<LatLng>?,//控制点
        @ColumnInfo(name = "task_waypoint") var wayPointList: List<LatLng>,//执行任务
        @ColumnInfo(name = "task_last_complete") var taskLastComplete: Int,//上次的完成点
        @ColumnInfo(name = "img_path") var imgPath: String?,//任务截图路径
        @ColumnInfo(name = "task_type") var taskType: TaskTypeEnum//任务类型


)