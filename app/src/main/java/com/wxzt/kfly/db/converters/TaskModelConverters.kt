package com.wxzt.kfly.db.converters

import androidx.room.TypeConverter
import com.blankj.utilcode.util.GsonUtils
import com.wxzt.kfly.db.entity.FlySettingEntity
import com.wxzt.kfly.enums.TaskModelEnum
import com.wyzt.lib_common.ext.utils.toEnum
import com.wyzt.lib_common.ext.utils.toInt


/**
 * @Author:          zuoz
 * @CreateDate:     2021/1/13 16:20
 * @Description:    room 存放date
 */
class TaskModelConverters {
    @TypeConverter
    fun fromTimestamp(value: Int): TaskModelEnum {
        return value.toEnum()
    }

    @TypeConverter
    fun dateToTimestamp(date: TaskModelEnum): Int {
        return date.toInt()
    }
}