package com.wxzt.kfly.db.converters

import androidx.room.TypeConverter
import com.amap.api.maps.model.LatLng
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import com.wxzt.kfly.entitys.WayPoint
import java.util.*


/**
 * @Author:          zuoz
 * @CreateDate:     2021/1/13 16:20
 * @Description:    room 存放date
 */
class ListConverters  {
    @TypeConverter
    fun fromTimestamp(value: String): List<LatLng?>? {
        return value?.let { GsonUtils.fromJson(value, object : TypeToken<List<LatLng>>() {}.type) }
    }

    @TypeConverter
    fun dateToTimestamp(date: List<LatLng?>?): String? {
        return date?.let { GsonUtils.toJson(it) }
    }
}