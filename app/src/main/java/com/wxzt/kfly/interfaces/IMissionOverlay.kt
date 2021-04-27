package com.wxzt.kfly.interfaces

import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker

/**
 *  author      : zuoz
 *  date        : 2021/4/9 10:57
 *  description :
 */
interface IMissionOverlay {
    fun controlMarkersChange(marker: Marker?, controlMarkers: MutableList<LatLng>)
}