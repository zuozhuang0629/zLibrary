package com.wxzt.kfly.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wxzt.kfly.enums.TaskModelEnum
import java.util.*

/**
 *  author      : zuoz
 *  date        : 2021/4/9 8:41
 *  description :飞行设置实体
 */
@Entity(tableName = "task_fly_setting")
data class FlySettingEntity(
        @ColumnInfo(name = "task_model") var taskModel: TaskModelEnum,//任务飞行模式
        @ColumnInfo(name = "task_speed") var taskSpeed: Int,//飞行速度
        @ColumnInfo(name = "take_off_speed") var takeOffSpeed: Int,//起飞速度
        @ColumnInfo(name = "fly_height") var flyHeight: Int,//飞行高度
        @ColumnInfo(name = "horizontal_overlap") var horizontalOverlap: Float,//旁向
        @ColumnInfo(name = "vertical_overlap") var verticalOverlap: Float,//航向
        @ColumnInfo(name = "task_complete") var taskComplete: Int//完成进度 0%~90%

) {
}