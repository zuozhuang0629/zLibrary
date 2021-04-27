package com.wxzt.kfly.interfaces

import com.amap.api.maps.model.LatLng

/**
 *  author      : zuoz
 *  date        : 2021/4/8 15:15
 *  description :地图定位改变
 */
interface ILocationChange {
    fun locationChange(location: LatLng)
}