package com.wxzt.kfly.overlays

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import com.amap.api.maps.AMap
import com.amap.api.maps.model.*
import com.wxzt.kfly.KFlyApplication
import com.wxzt.kfly.interfaces.IMissionOverlay
import com.wxzt.lib_common.R

import java.util.*

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/12/2 10:37
 * Description: 定点拍照图层
 * History:
 */
class FixOverlay(private val mAMap: AMap, val missionCallBack: IMissionOverlay) {
    private var mTaskLatLng: MutableList<LatLng>
    private val mMarkers: MutableList<Marker>
    private val context: Context
    private var polylineOptions: PolylineOptions
    private var mPolyline: Polyline
    private var mIsStart = false
    fun drawServerTemplate(controlPointsList: List<LatLng>) {
        for (marker in mMarkers) {
            marker.remove()
        }
        mTaskLatLng.clear()
        mMarkers.clear()
        for (latLng in controlPointsList) {
            addMarkers(latLng)
            addFixPath()
        }
    }

    private fun addFixPath() {
        mPolyline.points = mTaskLatLng
    }

    fun onMapClick(latLng: LatLng) {
        if (!mIsStart) {
            return
        }
        addMarkers(latLng)
        addFixPath()
        refreshTasks()
    }

    /**
     * 添加markers
     *
     * @param latLng
     */
    @Synchronized
    private fun addMarkers(latLng: LatLng) {
        mTaskLatLng.add(latLng)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
                .anchor(0.5f, 1f)
                .draggable(true)
                .icon(getFixBitMap(mTaskLatLng.size, context))
        val marker = mAMap.addMarker(markerOptions)
        mMarkers.add(marker)
    }

    /**
     * 在map图标上绘制数
     *
     * @param text---需要绘制的文字
     * @param context---获取资源的上下文
     * @return 图片和文字合在一起的bitmap
     */
    private fun getFixBitMap(text: Int, context: Context): BitmapDescriptor {
        var bitmap = BitmapFactory.decodeResource(context.resources,
                R.drawable.ic_fix_marker).copy(Bitmap.Config.ARGB_8888, true)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height)
        val canvas = Canvas(bitmap)

        // 设置文字样式，大小，颜色
        val textPaint = TextPaint()
        textPaint.setAntiAlias(true)
        textPaint.setColor(Color.rgb(226, 138, 34))
        textPaint.setTextSize(35f)
        textPaint.setFakeBoldText(true)
        textPaint.setTextAlign(Paint.Align.CENTER)
        canvas.drawText(text.toString(), (canvas.width * 19 / 40).toFloat(), (
                canvas.height * 11 / 20).toFloat(), textPaint)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun setStartAddData(isStart: Boolean) {
        mIsStart = isStart
    }

    fun clearData() {
        for (marker in mMarkers) {
            marker.remove()
        }
        mTaskLatLng = ArrayList()
        mMarkers.clear()
        addFixPath()
    }

    fun onMarkerDragStart(marker: Marker) {
        val markerIndex = mMarkers.indexOf(marker)
        mTaskLatLng[markerIndex] = marker.position
    }

    fun onMarkerDrag(marker: Marker) {
        val markerIndex = mMarkers.indexOf(marker)
        mTaskLatLng[markerIndex] = marker.position
        mPolyline.points = mTaskLatLng
    }

    fun onMarkerDragEnd(marker: Marker) {
        refreshTasks()
    }

    fun delLastPoint() {
        val marker = mMarkers[mMarkers.size - 1]
        mMarkers.removeAt(mMarkers.size - 1)
        marker.remove()
        marker.destroy()
        mTaskLatLng.removeAt(mTaskLatLng.size - 1)
        mPolyline.points = mTaskLatLng
    }

    fun onMarkerClick(marker: Marker): Boolean {
        if (!mMarkers.contains(marker)) {
            return false
        }
        delRefresh(marker)
        return true
    }

    /**
     * 删除点刷新
     *
     * @param marker
     */
    private fun delRefresh(marker: Marker) {
        /* 刷新marker
         * 1.移除之前的maker
         * 2.修改后面maker对应的icon
         */
        val index = mMarkers.indexOf(marker)
        mMarkers.remove(marker)
        marker.remove()
        mTaskLatLng.removeAt(index)
        markerIconRefresh(index)
        refreshTasks()
        //
        /* *****************刷新marker*************/
        /* ********刷新线*********************/mPolyline.points = mTaskLatLng
    }

    /**
     * 刷新后面marker的icon
     */
    private fun markerIconRefresh(refreshIndex: Int) {
        for (index in refreshIndex until mMarkers.size) {
            val marker = mMarkers[index]
            marker.setIcon(getFixBitMap(index + 1, context))
        }
    }

    private fun refreshTasks() {
        missionCallBack.controlMarkersChange(null, mTaskLatLng)
    }

    fun drawLocationMission(task: List<LatLng>,
                            seek: Int) {
        //清空之前的数据
        for (marker in mMarkers) {
            marker.remove()
        }
        mMarkers.clear()
        mTaskLatLng.clear()
        for (latLng in task) {
            addMarkers(latLng)
            addFixPath()
        }
    }

    fun setFinishStatus() {
        mIsStart = true
        clearData()
    }

    fun setExecuteStatus() {
        for (marker in mMarkers) {
            marker.isDraggable = false
            marker.isClickable = false
        }
        mIsStart = false
    }

    init {

        context = KFlyApplication.app
        mTaskLatLng = ArrayList()
        mMarkers = ArrayList()
        polylineOptions = PolylineOptions()
        polylineOptions.width(5f)
        polylineOptions.color(Color.rgb(255, 121, 24))
        polylineOptions.isDottedLine = true
        mPolyline = mAMap.addPolyline(polylineOptions)
    }


    fun isShowFixMissionPath(isShowPath: Boolean) {
        for (controlMarker in mMarkers) {
            controlMarker.isVisible = isShowPath
        }

        polylineOptions?.visible(isShowPath)
        mPolyline?.isVisible = isShowPath
    }

}