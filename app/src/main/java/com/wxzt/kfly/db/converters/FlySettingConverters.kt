package com.wxzt.kfly.db.converters

import androidx.room.TypeConverter
import com.blankj.utilcode.util.GsonUtils
import com.wxzt.kfly.db.entity.FlySettingEntity


/**
 * @Author:          zuoz
 * @CreateDate:     2021/1/13 16:20
 * @Description:    room 存放date
 */
class FlySettingConverters {
    @TypeConverter
    fun fromTimestamp(value: String): FlySettingEntity? {
        return value?.let { GsonUtils.fromJson(value, FlySettingEntity::class.java) }
    }

    @TypeConverter
    fun dateToTimestamp(date: FlySettingEntity?): String? {
        return date?.let { GsonUtils.toJson(it) }
    }
}