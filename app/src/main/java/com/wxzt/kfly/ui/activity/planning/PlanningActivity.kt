package com.wxzt.kfly.ui.activity.planning


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.ToastUtils
import com.wxzt.kfly.R
import com.wxzt.kfly.appViewModel
import com.wxzt.kfly.base.BasePlanningActivity
import com.wxzt.kfly.databinding.ActivityPolygonBinding
import com.wxzt.kfly.db.entity.FlySettingEntity
import com.wxzt.kfly.db.entity.LocationTaskEntity
import com.wxzt.kfly.enums.*
import com.wxzt.kfly.interfaces.ILocationChange
import com.wxzt.kfly.interfaces.IMissionOverlay
import com.wxzt.kfly.interfaces.ISettingChange
import com.wxzt.kfly.overlays.DJIUAVOverlay
import com.wxzt.kfly.overlays.TaskMissionControlOverlay
import com.wxzt.kfly.ui.dialog.SettingDialog
import com.wxzt.kfly.ui.dialog.TaskOffCheckDialog
import com.wxzt.kfly.ustils.MapUtils
import com.wxzt.kfly.viewmodel.PlanningVM
import com.wxzt.lib_common.ext.clickWithDuration
import com.wxzt.lib_common.ext.visibility
import dji.common.battery.BatteryState
import dji.common.camera.StorageState
import dji.common.camera.SystemState
import dji.common.flightcontroller.FlightControllerState
import dji.common.gimbal.GimbalState
import dji.common.mission.waypoint.WaypointMissionExecutionEvent
import dji.common.mission.waypoint.WaypointMissionUploadEvent
import dji.common.remotecontroller.HardwareState
import dji.sdk.media.MediaFile
import java.io.File


class PlanningActivity : BasePlanningActivity<ActivityPolygonBinding>(),
        ILocationChange, ISettingChange, IMissionOverlay, View.OnClickListener, AMap.OnMapScreenShotListener {

    private lateinit var mMap: AMap

    private val mViewModel: PlanningVM by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(PlanningVM::class.java)
    }

    private val userName: String by lazy {
        appViewModel.userInfo.value!!.userName
    }
    private val authorization: String by lazy {
        appViewModel.userInfo.value!!.getRequestToken()
    }

    private val mTask: LocationTaskEntity?
        get() {
            return mViewModel.task.value
        }

    private lateinit var djiUavOverlay: DJIUAVOverlay
    private lateinit var taskMissionControlOverlay: TaskMissionControlOverlay
    private val calculationManagement: CalculationManagement by lazy {
        CalculationManagement()
    }

    override fun getViewBinding(): ActivityPolygonBinding = ActivityPolygonBinding.inflate(layoutInflater)

    //region  生命周期
    override fun onResume() {
        super.onResume()
        mViewBinding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mViewBinding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewBinding.mapView.onDestroy()
        appViewModel.taskInfo.value = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mViewBinding.mapView.onSaveInstanceState(outState)
    }

    //endregion

    override fun initView(savedInstanceState: Bundle?) {
        initMap(savedInstanceState)
        drawMission()
    }

    /**
     * 绘制任务
     *
     */
    private fun drawMission() {
        mTask?.let {
            when (it.flySettingEntity.taskModel) {
                TaskModelEnum.POLYGON, TaskModelEnum.OBLIQUE -> {
                    taskMissionControlOverlay.drawPolygonMission(it.taskControl?.toMutableList(),
                            it.wayPointList.toMutableList(), 0)
                }
                TaskModelEnum.FIX -> {
                    taskMissionControlOverlay.drawFixMission(it.taskControl?.toMutableList())
                }
            }
        }

    }

    override fun initData() {
        appViewModel.taskInfo.value?.let {
            mViewModel.task.value = appViewModel.taskInfo.value!!
            initServiceIU()
        } ?: let {
            initLocationUI()
        }
    }

    override fun initListener() {
        super.initListener()
        mViewBinding.planningSetting.clickWithDuration {
            showSettingDialog()
        }

        mViewModel.task.observe(this) { data ->
            showTaskModelUI(data.flySettingEntity.taskModel)
            drawMission()
        }
        mViewBinding.planningBtnFly.setOnClickListener(this)
        mViewBinding.planningAddFix.setOnClickListener(this)
        mViewBinding.planningFixComplete.setOnClickListener(this)
        mViewBinding.planningHead.planningIvHome.setOnClickListener(this)
        mViewBinding.planningLive.setOnClickListener(this)
    }

    override fun locationChange(location: LatLng) {
        if (mTask?.taskType == TaskTypeEnum.SERVICE) {
            return
        }

        if (mTask == null) {
            mViewModel.getDefLocation(userName, location)
        }

    }

    private fun initMap(savedInstanceState: Bundle?) {
        mViewBinding.mapView.onCreate(savedInstanceState)
        mMap = mViewBinding.mapView.map
        taskMissionControlOverlay = TaskMissionControlOverlay(mMap, this)

        val drone = BitmapFactory.decodeResource(this.resources, R.drawable.ic_uav_map)
        djiUavOverlay = DJIUAVOverlay(drone, mMap)
        MapUtils(mMap, this, this)
    }

    /**
     * 显示设置界面
     *
     */
    private fun showSettingDialog() {
        SettingDialog(mTask?.flySettingEntity, this).show(supportFragmentManager, "")
    }

    override fun setDataChanged(flySettingEntity: FlySettingEntity?) {
        mTask?.let {
            flySettingEntity?.let { settitng ->
                it.flySettingEntity = settitng
                taskMissionControlOverlay.setTaskModel(it.flySettingEntity.taskModel)
                showTaskModelUI(it.flySettingEntity.taskModel)
                when (it.flySettingEntity.taskModel) {
                    TaskModelEnum.POLYGON, TaskModelEnum.OBLIQUE -> {

                        val newRoute = calculationManagement.calculationRoute(it.taskControl, it.flySettingEntity.flyHeight,
                                it.flySettingEntity.horizontalOverlap)

                        taskMissionControlOverlay.polygonSettingRefreshMissionPath(newRoute, it.flySettingEntity.taskComplete)

                    }
                    TaskModelEnum.PANORAMIC -> {

                    }
                    TaskModelEnum.FIX -> {

                    }
                }
            }

        }

    }

    override fun controlMarkersChange(marker: Marker?, controlMarkers: MutableList<LatLng>) {
        mTask?.let {
            when (it.flySettingEntity.taskModel) {
                TaskModelEnum.OBLIQUE, TaskModelEnum.POLYGON -> {
                    it.taskControl = controlMarkers
                    val newRoute = calculationManagement.calculationRoute(it.taskControl, it.flySettingEntity.flyHeight,
                            it.flySettingEntity.horizontalOverlap)
                    taskMissionControlOverlay.refreshPolygonMissionPath(marker!!, newRoute, it.flySettingEntity.taskComplete)
                }

                TaskModelEnum.FIX -> {
//                    it.taskControl = controlMarkers
                    it.wayPointList = controlMarkers
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.planning_btn_fly -> {
                mMap.getMapScreenShot(this)
                mTask?.let {
                    TaskOffCheckDialog(it).show(supportFragmentManager, "")
                }
            }

            R.id.planning_add_fix -> {
                taskMissionControlOverlay.setFixStartAdd(true)
            }
            R.id.planning_fix_complete -> {
                taskMissionControlOverlay.setFixStartAdd(false)
            }
            R.id.planning_iv_home -> {
                this.finish()
            }
            R.id.planning_live -> {
                mViewBinding.planningLive.isSelected = !mViewBinding.planningLive.isSelected
                mViewModel.startLive(userName)
            }
            R.id.planning_over_btn -> {
                mViewModel.startGoHome()
            }
            R.id.planning_resume_btn -> {
                mViewModel.resumeOrStopMission()
            }
            else -> {
            }
        }
    }

    //region dji
    /**
     * 相机回调
     *
     * @param systemState
     */
    override fun setCameraListener(systemState: SystemState) {

    }

    /**
     * 内存回调
     *
     * @param storageState
     */
    override fun setStorageStateListener(storageState: StorageState) {

    }

    /**
     * 拍照文件
     *
     * @param mediaFile
     */
    override fun setMediaFileListener(mediaFile: MediaFile) {
        if (mTask?.taskType == TaskTypeEnum.SERVICE
                && mTask?.taskStatus == TaskStatusEnum.EXECUTING) {

            mViewModel.uploadPreviewInfo(mediaFile, userName, authorization)
        }
    }

    /**
     * 电池状态
     *
     * @param batteryState
     */
    override fun setBatteryStateListener(batteryState: BatteryState) {
        mViewModel.djiBatteryStatus.postValue(batteryState)
    }

    /**
     * 设置档位变化
     *
     * @param hardwareState
     */
    override fun setHardwareStateListener(hardwareState: HardwareState) {
    }

    /**
     * 无人机状态监听
     *
     * @param flightControllerState
     */
    override fun setFlightControllerStateListener(flightControllerState: FlightControllerState) {
        mViewModel.djiFlightControllerState.postValue(flightControllerState)
        djiUavOverlay.refreshUAVPosition(flightControllerState)
        if (mTask?.taskType == TaskTypeEnum.SERVICE) {
            mViewModel.updateUAVInfo(flightControllerState, userName, authorization)
        }
    }

    override fun setLiveStatusListener(liveStatusEnum: LiveStatusEnum) {
        mViewBinding.planningLive.isSelected = liveStatusEnum == LiveStatusEnum.LIVE_SUCCESS
        runOnUiThread {
            mViewBinding.newKFlyFPV.showLiveInfo(liveStatusEnum)
        }

    }

    /**
     *  设置任务上传
     *
     * @param waypointMissionUploadEvent
     */
    override fun setMissionUploadUpdateListener(waypointMissionUploadEvent: WaypointMissionUploadEvent) {
        appViewModel.djiWaypointMissionUploadEvent.postValue(waypointMissionUploadEvent)
    }

    /**
     *  设置执行
     *
     * @param waypointMissionUploadEvent
     */
    override fun setExecutionUpdateListener(waypointMissionExecutionEvent: WaypointMissionExecutionEvent) {
        val progress = waypointMissionExecutionEvent.progress
        progress?.let {
            //刷新完成点
            mTask?.let {
                it.taskLastComplete =
                        if (progress.isWaypointReached) progress.targetWaypointIndex else progress.targetWaypointIndex - 1
                mViewModel.updateLocationDb(it)

                taskMissionControlOverlay.updateComplete(it.flySettingEntity.taskModel,
                        it.taskLastComplete)

                ToastUtils.showLong(it.taskLastComplete)
            }
        } ?: let {
            ToastUtils.showLong("无人机完成进度返回错误,飞行数据会丢失！")
        }
    }

    /**
     *  任务开始
     *
     */
    override fun setExecutionStartListener() {
        mTask?.let {
            mViewModel.saveLocationDb(it)
            it.taskStatus = TaskStatusEnum.EXECUTING
            mViewModel.updateMissionState(authorization)
            //截图
            mMap.getMapScreenShot(this)

            setExecutionMissionUI(it.taskStatus)
        }
    }

    override fun setExecutionFinishListener() {

        mTask?.let {
            //判断任务知否执行完成
            if (it.wayPointList.size == it.flySettingEntity.taskComplete
                    || it.wayPointList.size == it.flySettingEntity.taskComplete - 1) {
                //任务全部完成
                it.taskStatus = TaskStatusEnum.COMPLETE
            } else {
                it.taskStatus = TaskStatusEnum.STOP
            }

            setExecutionMissionUI(it.taskStatus)
        }

    }

    /**
     * 无人机产品状态监听
     *
     * @param djiProductStatus
     */
    override fun setProductStatusListener(djiProductStatus: DjiProductStatus) {
        when (djiProductStatus) {
            DjiProductStatus.PRODUCT_CONNECT -> {
                ToastUtils.showLong("连接无人机")
                mViewBinding.newKFlyFPV.visibility(true)
            }
            DjiProductStatus.PRODUCT_DISCONNECT -> {
                ToastUtils.showLong("断开连接")
                mViewBinding.newKFlyFPV.visibility(false)
            }
        }
    }

    /**
     *  设置万向节状态监听
     *
     */
    override fun setGimbalStateListener(gimbalState: GimbalState) {
        mViewModel.djiGimbalState.postValue(gimbalState)
    }

    //endregion

    //region
    /**
     * 显示任务对应UI
     *
     * @param taskModelEnum
     */
    private fun showTaskModelUI(taskModelEnum: TaskModelEnum) {
        when (taskModelEnum) {
            TaskModelEnum.OBLIQUE, TaskModelEnum.POLYGON -> {
                mViewBinding.planningAddFix.visibility(false)
                mViewBinding.planningFixComplete.visibility(false)
            }
            TaskModelEnum.FIX -> {
                mViewBinding.planningAddFix.visibility(true)
                mViewBinding.planningFixComplete.visibility(true)
            }
        }
    }


    private fun setExecutionMissionUI(status: TaskStatusEnum) {
        runOnUiThread {
            when (status) {
                TaskStatusEnum.EXECUTING -> {
                    mViewBinding.planningAddFix.visibility(false)
                    mViewBinding.planningFixComplete.visibility(false)
                    mViewBinding.planningBtnFly.visibility(false)
                    mViewBinding.planningResumeBtn.visibility(true)
                    mViewBinding.planningOverBtn.visibility(true)

                    taskMissionControlOverlay.setExecuteUI(mTask?.flySettingEntity?.taskModel)
                }
                TaskStatusEnum.COMPLETE -> {
                    mTask?.let {
                        when (it.flySettingEntity.taskModel) {
                            TaskModelEnum.FIX -> {
                                mViewBinding.planningAddFix.visibility(true)
                                mViewBinding.planningFixComplete.visibility(true)
                            }

                            TaskModelEnum.POLYGON, TaskModelEnum.OBLIQUE -> {
                                mViewBinding.planningAddFix.visibility(false)
                                mViewBinding.planningFixComplete.visibility(false)
                            }
                        }
                    }

                    mViewBinding.planningBtnFly.visibility(true)
                    mViewBinding.planningResumeBtn.visibility(false)
                    mViewBinding.planningOverBtn.visibility(false)
                }

                TaskStatusEnum.STOP -> {
                    mViewBinding.planningBtnFly.visibility(true)
                    mViewBinding.planningResumeBtn.visibility(false)
                    mViewBinding.planningOverBtn.visibility(false)
                }
            }
        }
    }


    private fun setCompleteUI() {
        runOnUiThread {

        }

    }

    override fun onMapScreenShot(p0: Bitmap?) {

    }

    override fun onMapScreenShot(bitmap: Bitmap?, p1: Int) {
        bitmap?.let {
            val imgPath = PathUtils.getExternalAppPicturesPath() + File.separator + mViewModel.task.value!!.missionId + ".png"
            mViewModel.getScreenShot(it, imgPath, {
                mViewModel.task.value!!.imgPath = imgPath
                mViewModel.task.value!!
            }, {

            })

        }
    }

    /**
     * 显示本地任务Ui
     *
     */
    private fun initLocationUI() {
        mViewBinding.planningLive.visibility(false)
    }

    /**
     * 显示平台任务Ui
     *
     */
    private fun initServiceIU() {
        mViewBinding.planningLive.visibility(true)
    }


    //endregion

}