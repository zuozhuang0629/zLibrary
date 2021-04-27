package com.wxzt.kfly.db.converters

import androidx.room.TypeConverter
import java.util.*


/**
 * @Author:          zuoz
 * @CreateDate:     2021/1/13 16:20
 * @Description:    room 存放date
 */
class DateConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long? {
        return date.time
    }
}