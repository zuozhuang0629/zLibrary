package com.wxzt.kfly.db.converters

import androidx.room.TypeConverter
import com.blankj.utilcode.util.GsonUtils
import com.wxzt.kfly.db.entity.FlySettingEntity
import com.wxzt.kfly.enums.TaskModelEnum
import com.wxzt.kfly.enums.TaskStatusEnum
import com.wyzt.lib_common.ext.utils.toEnum
import com.wyzt.lib_common.ext.utils.toInt


/**
 * @Author:          zuoz
 * @CreateDate:     2021/1/13 16:20
 * @Description:    room 存放date
 */
class TaskStatusConverters {
    @TypeConverter
    fun fromTimestamp(value: Int): TaskStatusEnum {
        return value.toEnum()
    }

    @TypeConverter
    fun dateToTimestamp(date: TaskStatusEnum): Int {
        return date.toInt()
    }
}