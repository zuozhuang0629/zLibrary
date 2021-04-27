package com.wxzt.kfly.overlays

import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.wxzt.kfly.enums.TaskModelEnum
import com.wxzt.kfly.interfaces.IMissionOverlay

/**
 *  author      : zuoz
 *  date        : 2021/4/14 17:01
 *  description :任务航线控制
 */
class TaskMissionControlOverlay(private val mAMap: AMap, private val missionCallBack: IMissionOverlay) :
        AMap.OnMarkerClickListener, AMap.OnMarkerDragListener, AMap.OnMapClickListener {

    //是否允许用户交互
    private var isTouch = true
    private var currentModel = TaskModelEnum.POLYGON
    private val polygonMissionPathOverlay: PolygonMissionPathOverlay by lazy { PolygonMissionPathOverlay(mAMap, missionCallBack) }
    private val fixOverlay: FixOverlay by lazy { FixOverlay(mAMap, missionCallBack) }

    init {
        mAMap.setOnMarkerClickListener(this)
        mAMap.setOnMarkerDragListener(this)
        mAMap.setOnMapClickListener(this)
    }

    /**
     * 多边形的设置界面刷新
     *
     * @param tasks
     * @param seek
     */
    fun polygonSettingRefreshMissionPath(tasks: List<LatLng>, seek: Int) {
        polygonMissionPathOverlay.settingRefreshMissionPath(tasks, seek)
    }

    /**
     * 多边形 拖动刷新
     *
     * @param marker
     * @param tasks
     * @param seek
     */
    fun refreshPolygonMissionPath(marker: Marker, tasks: List<LatLng>, seek: Int) {
        polygonMissionPathOverlay.refreshMissionPath(marker, tasks, seek)
    }

    /**
     * 绘制多边形
     *
     * @param controlPoints
     * @param task
     * @param seek
     */
    fun drawPolygonMission(controlPoints: MutableList<LatLng>?, task: MutableList<LatLng>, seek: Int) {
        polygonMissionPathOverlay.drawMission(controlPoints, task, seek)
    }

    /**
     * 绘制fix
     *
     * @param task
     */
    fun drawFixMission(task: MutableList<LatLng>?) {
        task?.let { fixOverlay.drawServerTemplate(it) }
    }

    fun setTaskModel(model: TaskModelEnum) {
        currentModel = model
        when (model) {
            TaskModelEnum.POLYGON, TaskModelEnum.OBLIQUE -> {
                polygonMissionPathOverlay.isShowPolgonMissionPath(true)
                fixOverlay.isShowFixMissionPath(false)
            }

            TaskModelEnum.FIX -> {
                polygonMissionPathOverlay.isShowPolgonMissionPath(false)
                fixOverlay.isShowFixMissionPath(true)

            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (!isTouch) {
            return true
        }

        when (currentModel) {
            TaskModelEnum.POLYGON, TaskModelEnum.OBLIQUE -> {
                polygonMissionPathOverlay.onMarkerClick(marker)
            }
            TaskModelEnum.FIX -> {
                fixOverlay.onMarkerClick(marker)
            }
        }

        return true
    }

    override fun onMarkerDragStart(marker: Marker) {
        if (!isTouch) {
            return
        }
        when (currentModel) {
            TaskModelEnum.FIX -> {
                fixOverlay.onMarkerDragStart(marker)
            }
        }

    }

    override fun onMarkerDrag(marker: Marker) {
        if (!isTouch) {
            return
        }
        when (currentModel) {
            TaskModelEnum.POLYGON, TaskModelEnum.OBLIQUE -> {
                polygonMissionPathOverlay.onMarkerDrag(marker)
            }
            TaskModelEnum.FIX -> {

            }
        }
    }

    override fun onMarkerDragEnd(marker: Marker) {
        if (!isTouch) {
            return
        }
        when (currentModel) {
            TaskModelEnum.FIX -> {
                fixOverlay.onMarkerDragEnd(marker)
            }
        }
    }

    override fun onMapClick(latLng: LatLng) {
        if (!isTouch) {
            return
        }
        when (currentModel) {
            TaskModelEnum.FIX -> {
                fixOverlay.onMapClick(latLng)
            }
        }

    }

    fun setFixStartAdd(isStart: Boolean) {
        fixOverlay.setStartAddData(isStart)
    }


    fun updateComplete(model: TaskModelEnum, seek: Int) {
        when (model) {
            TaskModelEnum.FIX -> {
            }
            TaskModelEnum.POLYGON, TaskModelEnum.OBLIQUE -> {
               polygonMissionPathOverlay.refreshCompleteMissionPath(seek)
            }
            TaskModelEnum.PANORAMIC -> {

            }
        }

    }

    fun setExecuteUI(model: TaskModelEnum?) {
        when (model) {
            TaskModelEnum.FIX -> {

            }
            TaskModelEnum.POLYGON, TaskModelEnum.OBLIQUE -> {
                setPolygonExecute()

            }
            TaskModelEnum.PANORAMIC -> {

            }
        }
    }

    private fun setPolygonExecute() {
        polygonMissionPathOverlay.setExecuteStatus()
    }
}