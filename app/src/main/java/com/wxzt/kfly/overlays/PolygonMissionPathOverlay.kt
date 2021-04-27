package com.wxzt.kfly.overlays

import android.graphics.Color
import com.amap.api.maps.AMap
import com.amap.api.maps.model.*
import com.blankj.utilcode.util.CollectionUtils

import com.wxzt.kfly.R
import com.wxzt.kfly.interfaces.IMissionOverlay
import java.util.*

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/28 16:03
 * Description: 任务图层 (多边形)
 * History:
 */
class PolygonMissionPathOverlay(val mAMap: AMap, val missionCallBack: IMissionOverlay) {

    private var mControlMarkers: MutableList<Marker> = mutableListOf()//控制点marker
    private var mControlPoints: MutableList<LatLng> = mutableListOf()
    private var mRouteAreaPolygon: Polygon? = null//航线面积
    private var mWayMissionPolyline: Polyline? = null //任务点line
    private var mControlPointBitmap: BitmapDescriptor?//控制点图标
    private var mStartPointBitmap: BitmapDescriptor?//起始点
    private var mAddBitmap: BitmapDescriptor?//添加
    private var mStartMarker: Marker? = null//开始marker
    private var mAddMarkers: MutableList<Marker> = mutableListOf()//添加控制点
    private var mTasks: MutableList<LatLng>? = null//火星坐标

    init {
        mControlPointBitmap = BitmapDescriptorFactory.fromResource(R.drawable.control_point)
        mStartPointBitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_start_point)
        mAddBitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_add)

    }

    fun drawMission(controlPoints: MutableList<LatLng>?, task: MutableList<LatLng>, seek: Int) {

        //清空之前的数据
        for (marker in mAddMarkers) {
            marker.remove()
        }
        mAddMarkers.clear()
        for (marker in mControlMarkers) {
            marker.remove()
        }
        mControlMarkers.clear()
        mControlPoints.clear()
        mTasks?.clear()
        mTasks = task

        //重新绘制
        controlPoints?.let {
            if (it.size > 1 && it[0] == it[it.size - 1]) {
                it.removeAt(it.size - 1)
            }

            drawControlPoints(it)
            drawAddMarker(it)
        }

        drawPolygonArea()
        drawWayMission(task, seek)
    }

    private fun drawStartPoint(latLng: LatLng) {
        if (null != mStartMarker) {
            mStartMarker?.remove()
        }
        val markerOption = MarkerOptions()
        markerOption.position(latLng)
                .draggable(false) //设置Marker可拖动
                .icon(mStartPointBitmap)
                .anchor(0.5f, 1f)
        mStartMarker = mAMap.addMarker(markerOption)
    }

    /**
     * 移除marker
     *
     * @param markers
     */
    private fun removeMaker(markers: MutableList<Marker>) {
        val isEmpty = CollectionUtils.isEmpty(markers) || markers.isEmpty()
        if (isEmpty) {
            return
        }
        for (marker in markers) {
            marker.remove()
        }
        markers.clear()
    }

    /**
     * 绘制控制点
     *
     * @param controlPoints
     */
    private fun drawControlPoints(controlPoints: MutableList<LatLng>) {
        removeMaker(mControlMarkers)
        mControlPoints = controlPoints
        for (index in controlPoints.indices) {
            val latLng = controlPoints[index]
            addControlMarker(latLng)
        }
    }

    /**
     * 添加控制点marker
     *
     * @param latLng
     */
    private fun addControlMarker(latLng: LatLng) {
        val markerOption = MarkerOptions()
        markerOption.position(latLng)
                .draggable(true) //设置Marker可拖动
                .icon(mControlPointBitmap)
                .anchor(0.5f, 0.5f)
        mControlMarkers.add(mAMap.addMarker(markerOption))
    }

    /**
     * 绘制添加控制点的marker
     */
    private fun drawAddMarker(controlPoints: List<LatLng>) {
        removeMaker(mAddMarkers)
        //遍历控制点  在两个控制点中加  添加点
        for (index in controlPoints.indices) {
            val one = controlPoints[index]
            var tow: LatLng

            //判断是否为最后一个
            tow = if (index + 1 > controlPoints.size - 1) {
                controlPoints[0]
            } else {
                controlPoints[index + 1]
            }
            val latLng = LatLng((one.latitude + tow.latitude) / 2, (one.longitude + tow.longitude) / 2)
            addAddMarker(latLng)
        }
    }

    /**
     * 添加add  marker
     *
     * @param latLng
     */
    private fun addAddMarker(latLng: LatLng) {
        val markerOption = MarkerOptions()
        markerOption.position(latLng)
                .draggable(false) //设置Marker可拖动
                .icon(mAddBitmap)
                .anchor(0.5f, 0.5f)
        val marker: Marker = mAMap.addMarker(markerOption)
        mAddMarkers.add(marker)
    }

    /**
     * 绘制多边形区域
     */
    private fun drawPolygonArea() {

        mRouteAreaPolygon?.remove()

        val polygonOptions = PolygonOptions()
        polygonOptions.fillColor(Color.argb(100, 0, 0, 0))
                .strokeColor(Color.rgb(1, 159, 85))
                .strokeWidth(3f)
        for (marker in mControlMarkers) {
            polygonOptions.add(marker.position)
        }
        mRouteAreaPolygon = mAMap.addPolygon(polygonOptions)
    }


    fun refreshMissionPath(marker: Marker, tasks: List<LatLng>, seek: Int) {
        mTasks = tasks.toMutableList()
        refreshAddPoint(marker)
        drawPolygonArea()
        drawWayMission(mTasks, seek)
    }

    fun settingRefreshMissionPath(tasks: List<LatLng>, seek: Int) {
        mTasks = tasks.toMutableList()
        drawPolygonArea()
        drawWayMission(mTasks, seek)
    }

    fun refreshCompleteMissionPath(seek: Int) {
        mTasks?.let {
            drawCompleteWayMission(seek)
        }

    }

    /**
     * 刷新添加控制点
     *
     * @param refreshMarker
     */
    private fun refreshAddPoint(refreshMarker: Marker) {
        val index = mControlMarkers.indexOf(refreshMarker)
        val latLng = refreshMarker.position
        if (index == 0) {
            val one = mAddMarkers[0]
            val tow = mAddMarkers[mAddMarkers.size - 1]
            val oneLatLng = mControlPoints[index + 1]
            val towLatLng = mControlPoints[mControlPoints.size - 1]
            one.position = LatLng((latLng.latitude + oneLatLng.latitude) / 2, (latLng.longitude + oneLatLng.longitude) / 2)
            tow.position = LatLng((latLng.latitude + towLatLng.latitude) / 2, (latLng.longitude + towLatLng.longitude) / 2)
        } else if (index == mControlMarkers.size - 1) {
            val one = mAddMarkers[index - 1]
            val tow = mAddMarkers[mAddMarkers.size - 1]
            val oneLatLng = mControlPoints[0]
            val towLatLng = mControlPoints[index - 1]
            tow.position = LatLng((latLng.latitude + oneLatLng.latitude) / 2, (latLng.longitude + oneLatLng.longitude) / 2)
            one.position = LatLng((latLng.latitude + towLatLng.latitude) / 2, (latLng.longitude + towLatLng.longitude) / 2)
        } else {
            val one = mAddMarkers[index - 1]
            val tow = mAddMarkers[index]
            val oneLatLng = mControlPoints[index - 1]
            val towLatLng = mControlPoints[index + 1]
            one.position = LatLng((latLng.latitude + oneLatLng.latitude) / 2, (latLng.longitude + oneLatLng.longitude) / 2)
            tow.position = LatLng((latLng.latitude + towLatLng.latitude) / 2, (latLng.longitude + towLatLng.longitude) / 2)
        }
    }

    private fun drawCompleteWayMission(seek: Int) {
        if (mTasks == null) {
            return
        }
        if (null != mWayMissionPolyline) {
            mWayMissionPolyline?.remove()
        }
        val polylineOptions = PolylineOptions()
        polylineOptions.width(5f)
        if (seek != 0) {
            val colors: MutableList<Int> = ArrayList()
            for (index in 0 until mTasks!!.size - 1) {
                if (index < seek) {
                    colors.add(Color.YELLOW)
                } else {
                    colors.add(Color.rgb(159, 202, 61))
                }
            }
            polylineOptions.colorValues(colors)
        } else {
            polylineOptions.color(Color.rgb(159, 202, 61))
        }
        polylineOptions.points = mTasks
        mWayMissionPolyline = mAMap.addPolyline(polylineOptions)

    }


    /**
     * 用于设置界面百分比
     *
     * @param tasks
     * @param seek 1 ~100百分百
     */
    private fun drawWayMission(tasks: MutableList<LatLng>?, seek: Int) {
        if (null == tasks) {
            return
        }

        var seek = (seek / 100f * tasks.size).toInt()
        seek = Math.max(0, seek)

        if (tasks.size == 0) {
            return
        }
        if (null != mWayMissionPolyline) {
            mWayMissionPolyline?.remove()
        }
        val polylineOptions = PolylineOptions()
        polylineOptions.width(5f)
        if (seek != 0) {
            val colors: MutableList<Int> = ArrayList()
            for (index in 0 until tasks.size - 1) {
                if (index < seek) {
                    colors.add(Color.YELLOW)
                } else {
                    colors.add(Color.rgb(159, 202, 61))
                }
            }
            polylineOptions.colorValues(colors)
        } else {
            polylineOptions.color(Color.rgb(159, 202, 61))
        }
        polylineOptions.points = tasks
        mWayMissionPolyline = mAMap.addPolyline(polylineOptions)
        drawStartPoint(tasks[seek])
    }

    fun onMarkerClick(marker: Marker) {
        //判断是否为添加点
        if (!mAddMarkers.contains(marker)) {
            return
        }
        val index = mAddMarkers.indexOf(marker)
        //判断是否为第一个
        if (index == mAddMarkers.size - 1) { //判断是否为最后一个
            val last = mControlMarkers[index]
            val next = mControlMarkers[0]
            addAddControlMarker(index, last, marker)
            addAddControlMarker(index + 1, marker, next)
            //将添加控制点插入控制点中
            mControlPoints.add(index + 1, marker.position)
            marker.isClickable = false
            marker.setIcon(mControlPointBitmap)
            marker.isDraggable = true
            mControlMarkers.add(index + 1, marker)
            mAddMarkers.remove(marker)
        } else {
            val last = mControlMarkers[index]
            val next = mControlMarkers[index + 1]
            addAddControlMarker(index, last, marker)
            addAddControlMarker(index + 1, marker, next)

            //将添加控制点插入控制点中
            mControlPoints.add(index + 1, marker.position)
            marker.isClickable = false
            marker.setIcon(mControlPointBitmap)
            marker.isDraggable = true
            mControlMarkers.add(index + 1, marker)
            mAddMarkers.remove(marker)
        }

    }

    /**
     * 动态添加 添加控制点
     *
     * @param last
     * @param next
     */
    private fun addAddControlMarker(index: Int, last: Marker, next: Marker) {
        val lastLatLng = last.position
        val nextLatLng = next.position
        val latLng = LatLng((lastLatLng.latitude + nextLatLng.latitude) / 2f,
                (lastLatLng.longitude + nextLatLng.longitude) / 2f)
        val markerOption = MarkerOptions()
        markerOption.position(latLng)
                .draggable(true) //设置Marker可拖动
                .icon(mAddBitmap)
                .anchor(0.5f, 0.5f)
        val marker: Marker = mAMap.addMarker(markerOption)
        if (!mAddMarkers.contains(marker)) {
            mAddMarkers.add(index, marker)
        }
    }

    /**
     * destroy方法
     */
    fun destroy() {
        mControlMarkers.clear()
        mAddMarkers.clear()

        mRouteAreaPolygon?.remove()
        mRouteAreaPolygon = null


        mWayMissionPolyline?.remove()
        mWayMissionPolyline = null


        mControlPointBitmap?.recycle()
        mControlPointBitmap = null


        mStartPointBitmap?.recycle()
        mStartPointBitmap = null


        mAddBitmap?.recycle()
        mAddBitmap = null


        mStartMarker?.remove()
        mStartMarker?.destroy()
        mStartMarker = null

    }


    /**
     * 设置飞行只marker 状态   不能拖动与点击
     */
    fun setExecuteStatus() {
        for (marker in mControlMarkers) {
            marker.isDraggable = false
            marker.isClickable = false
        }

        for (marker in mAddMarkers) {
            marker.isDraggable = false
            marker.isClickable = false
            marker.isVisible = false
        }
    }


    fun onMarkerDrag(marker: Marker) {
        if (mControlMarkers.contains(marker)) {
            val index = mControlMarkers.indexOf(marker)
            val newControl = marker.position
            mControlPoints.removeAt(index)
            mControlPoints.add(index, newControl)
            missionCallBack.controlMarkersChange(marker, mControlPoints)
        }
    }


    fun isShowPolgonMissionPath(isShowPath: Boolean) {
        for (controlMarker in mControlMarkers) {
            controlMarker.isVisible = isShowPath
        }

        mRouteAreaPolygon?.isVisible = isShowPath
        mWayMissionPolyline?.isVisible = isShowPath
        mStartMarker?.isVisible = isShowPath

        for (addMarker in mAddMarkers) {
            addMarker.isVisible = isShowPath
        }
    }

}