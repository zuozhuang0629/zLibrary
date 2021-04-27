package com.wxzt.kfly.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.amap.api.maps.model.LatLng
import com.blankj.utilcode.util.*
import com.wxzt.kfly.appViewModel
import com.wxzt.kfly.db.KFlyDataBase
import com.wxzt.kfly.db.entity.FlySettingEntity
import com.wxzt.kfly.db.entity.LocationTaskEntity
import com.wxzt.kfly.dji.DJIDeviceHelper
import com.wxzt.kfly.dji.DJIEvent
import com.wxzt.kfly.dji.DJILiveHelper
import com.wxzt.kfly.dji.ICommonCallBack
import com.wxzt.kfly.entitys.WayPoint
import com.wxzt.kfly.enums.LiveStatusEnum
import com.wxzt.kfly.enums.TaskModelEnum
import com.wxzt.kfly.enums.TaskStatusEnum
import com.wxzt.kfly.enums.TaskTypeEnum
import com.wxzt.kfly.network.api.Api.LIVE_ADDRESS
import com.wxzt.kfly.network.apiService
import com.wxzt.kfly.ui.activity.planning.CalculationManagement
import com.wxzt.kfly.ustils.ZAMapUtil
import com.wxzt.lib_common.ext.launch
import com.wxzt.lib_common.ext.utils.buildJSONRequestBody
import com.wxzt.lib_common.viewmodel.BaseViewModel
import dji.common.battery.BatteryState
import dji.common.error.DJIError
import dji.common.flightcontroller.FlightControllerState
import dji.common.flightcontroller.LocationCoordinate3D
import dji.common.gimbal.GimbalState
import dji.common.mission.waypoint.WaypointMissionState
import dji.sdk.media.MediaFile
import dji.sdk.mission.MissionControl
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

/**
 *  author      : zuoz
 *  date        : 2021/4/8 15:21
 *  description :航线规划vm
 */
class PlanningVM : BaseViewModel() {

    var task = MutableLiveData<LocationTaskEntity>()
    var liveStatus = MutableLiveData<LiveStatusEnum>()

    val djiFlightControllerState = DJIEvent<FlightControllerState>()
    val djiGimbalState = DJIEvent<GimbalState>()
    val djiBatteryStatus = DJIEvent<BatteryState>()

    private val calculationManagement: CalculationManagement = CalculationManagement()

    /**
     * 根据定位获取航线
     *
     * @param center
     * @return
     */
    fun getDefLocation(userName: String, location: LatLng) {
        launch({
            val id = UUID.randomUUID().toString().replace("-", "").substring(0, 30)
            val horizontalOverlap = 0.85f
            val verticalOverlap = 0.85f
            val taskStatusEnum = TaskStatusEnum.NO_EXECUTE
            val taskModelEnum = TaskModelEnum.POLYGON
            val createTime = TimeUtils.getNowDate()
            val taskName = "本地任务_${TimeUtils.date2String(createTime)}"
            val taskSpeed = 5
            val takeOffSpeed = 10
            val height = 100

            val controlPoints = calculationManagement.getCenterRect(location)

            val wayPoints = getTaskWayPoints(location, controlPoints, horizontalOverlap,
                    taskModelEnum, height)
            val flySettingEntity = FlySettingEntity(taskModelEnum, taskSpeed, takeOffSpeed,
                    height, horizontalOverlap, verticalOverlap, 0)

            LocationTaskEntity(id, userName, taskName,
                    taskStatusEnum, createTime, flySettingEntity,
                    controlPoints, wayPoints, 0, "", TaskTypeEnum.LOCATION)

        }, {
            task.value = it
        }, {
            LogUtils.e(it)
        })
    }

    /**
     * 获取任务点（仅多边形和倾斜）
     *
     * @param location
     * @param horizontalOverlap
     * @param taskModelEnum
     * @param height
     * @return
     */
    private fun getTaskWayPoints(location: LatLng, controlPoints: List<LatLng>, horizontalOverlap: Float,
                                 taskModelEnum: TaskModelEnum, height: Int): List<LatLng> {

        if (taskModelEnum != TaskModelEnum.POLYGON && taskModelEnum != TaskModelEnum.OBLIQUE) {
            return listOf(location)
        }

        return calculationManagement.calculationRoute(controlPoints, height, horizontalOverlap)

    }

    /**
     * 更新本地数据完成点
     *
     */
    fun updateLocationDb(locationTaskEntity: LocationTaskEntity) {
        launch({
            KFlyDataBase.getInstance().locationTaskDao().updateLocationTask(locationTaskEntity)
        }, {

        }, {
            ToastUtils.showLong(it.message)
        })
    }

    fun saveLocationDb(locationTaskEntity: LocationTaskEntity) {
        launch({
            KFlyDataBase.getInstance().locationTaskDao().saveLocationTask(locationTaskEntity)
        }, {
            ToastUtils.showLong("任务保存数据库成功")
        }, {
            ToastUtils.showLong("任务保存数据库失败:${it.message}")
        })

    }

    /**
     * 获取任务截图
     *
     * @param imgPath 保存图片路径
     */
    fun getScreenShot(bitmap: Bitmap, imgPath: String,
                      success: () -> LocationTaskEntity,
                      error: (Throwable) -> Unit) {
        launch({
            val isSuccess = ImageUtils.save(bitmap, imgPath, Bitmap.CompressFormat.PNG)
            if (isSuccess) {
                val locationTaskEntity = success.invoke()
                KFlyDataBase.getInstance().locationTaskDao().updateLocationTask(locationTaskEntity)
            }
        }, {

        }, {
            error.invoke(it)
        })
    }

    /**
     * 开始直播
     *
     * @param userName
     */
    fun startLive(userName: String) {
        if (DJILiveHelper.instacne.isLiving()) {
            DJILiveHelper.instacne.stopLive()
            return
        }

        task.value?.let {

            launch({
                val deviceId: String = DeviceUtils.getAndroidID()
                val liveUrl = LIVE_ADDRESS + deviceId + "/" + it.missionId
                //开始直播
                DJILiveHelper.instacne.startLiveShow(liveUrl)

                updateLiveStatus(userName, deviceId, liveStatus.value!!.getStatusString(),
                        liveUrl, it.missionName, it.missionId)
            }, {

            }, {})

        } ?: let {
            liveStatus.value = LiveStatusEnum.LIVE_FAIL
        }
    }

    /**
     * 更新直播状态
     *
     * @param userName
     * @param deviceId
     * @param state
     * @param videoUrl
     */
    private suspend fun updateLiveStatus(userName: String, deviceId: String,
                                         state: String, videoUrl: String,
                                         taskName: String, taskId: String) {
        val datas = HashMap<String, String>()
        datas["deviceId"] = deviceId
        datas["userName"] = userName
        datas["state"] = state
        datas["taskName"] = taskName
        datas["taskId"] = taskId
        datas["videoUrl"] = videoUrl
        val ResponseBody = apiService.uploadLiveInfo(datas.buildJSONRequestBody())
    }

    /**
     * 上传缩略图
     *
     * @param fileBitmap
     */
    fun uploadPreviewInfo(mediaFile: MediaFile, userName: String, authorization: String) {
        launch({
            //bitmap存于本地
            val bitmap = mediaFile.preview
            val bitmapName = mediaFile.fileName
            val previewPath = PathUtils.getExternalAppPicturesPath() + "/" + "${bitmapName}_${TimeUtils.getNowMills()}"
            val isSave = ImageUtils.save(bitmap, previewPath, Bitmap.CompressFormat.JPEG, true)
            if (!isSave) {
                return@launch
            }

            uploadPreview(File(previewPath), userName, task.value!!.missionId,
                    task.value!!.flySettingEntity.taskModel.vlaue, authorization)
        }, {

        }, {
            ToastUtils.showLong("上传缩略图错误:${it.message}")
        })
    }

    /**
     * 上传任务的缩略图
     *
     * @param file
     * @param userName
     * @param taskId
     */
    private suspend fun uploadPreview(file: File, userName: String,
                                      taskId: String, taskType: String, authorization: String) {
        djiFlightControllerState.value?.let {
            val body: RequestBody = RequestBody.create(MediaType.parse("image/jpg"), file)
            val filePart = MultipartBody.Part.createFormData("file", file.name, body)
            // 添加描述    // 添加描述
            val taskIdRequestBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), taskId)
            val userNameRequestBody: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), userName)

            val locationCoordinate3D: LocationCoordinate3D = it.aircraftLocation
            val latLng: LatLng = ZAMapUtil.gps84_To_Gcj02(locationCoordinate3D.longitude, locationCoordinate3D.latitude)
            val aircraftHead: Int = it.aircraftHeadDirection
            djiGimbalState.value?.let { gimbalState ->
                val pitch = gimbalState.attitudeInDegrees.pitch
                apiService.uploadPreview(
                        userNameRequestBody,
                        taskIdRequestBody,
                        locationCoordinate3D.altitude,
                        latLng.latitude,
                        latLng.longitude,
                        pitch,
                        aircraftHead,
                        taskType,
                        filePart, authorization
                )
            }

        }

    }

    /**
     * 上传无人机实时信息
     *
     * @param userName
     */
    fun updateUAVInfo(djiFlightControllerState: FlightControllerState, userName: String, authorization: String) {
        launch({
            val locationTaskEntity = task.value
            val gimbalState = djiGimbalState.value
            val batteryState = djiBatteryStatus.value
            if (djiFlightControllerState != null
                    && locationTaskEntity != null
                    && gimbalState != null
                    && batteryState != null) {

                updateUAV(userName, djiFlightControllerState, locationTaskEntity,
                        gimbalState, batteryState.chargeRemainingInPercent, authorization)

            } else {
                false
            }

        }, {

        }, {

        })
    }

    /**
     * 上传无人机实时信息
     *
     * @param userName
     * @param flightControllerState
     * @param task
     * @param gimbalState
     * @param batteryPercent
     */
    private fun updateUAV(
            userName: String,
            flightControllerState: FlightControllerState,
            task: LocationTaskEntity, gimbalState: GimbalState,
            batteryPercent: Int, authorization: String) {
        val locationCoordinate3D: LocationCoordinate3D = flightControllerState.aircraftLocation
        val latLng: LatLng = ZAMapUtil.gps84_To_Gcj02(locationCoordinate3D.longitude, locationCoordinate3D.latitude)

        val latlng = ZAMapUtil.gps84_To_Gcj02(latLng.longitude, latLng.latitude)
        val parameter: MutableMap<String, String> = HashMap()

        parameter["latitude"] = latlng.latitude.toString()
        parameter["longitude"] = latlng.longitude.toString()
        parameter["taskId"] = task.missionId
        parameter["taskName"] = task.missionName
        parameter["speedX"] = flightControllerState.velocityX.toString()
        parameter["speedY"] = flightControllerState.velocityY.toString()
        parameter["speedZ"] = flightControllerState.velocityZ.toString()
        parameter["yawAngle"] = gimbalState.attitudeInDegrees.pitch.toString()
        parameter["altitude"] = locationCoordinate3D.altitude.toString()
        parameter["userName"] = userName
        parameter["deviceId"] = DeviceUtils.getAndroidID()
        parameter["batteryPercent"] = batteryPercent.toString()
        parameter["gps"] = flightControllerState.satelliteCount.toString()
        parameter["remoterControllerSignal"] = "0"
        parameter["pictureSignal"] = ""
        parameter["lineYaw"] = flightControllerState.aircraftHeadDirection.toString()
        parameter["status"] = task.taskStatus.vlaue

        val json: String = GsonUtils.toJson(parameter)
        val requestBody = RequestBody.create(MediaType.parse("application/json"), json)
        apiService.updateRealUAVData(requestBody, authorization)
    }


    fun updateMissionState(authorization: String) {
        launch({
            task.value?.let {
                updateMissionState(it, authorization)
            }
        }, {

        }, {

        })
    }

    private suspend fun updateMissionState(task: LocationTaskEntity, authorization: String) {
        val parameter: MutableMap<String, Any> = HashMap()
        parameter["id"] = task.missionId
        parameter["state"] = task.taskStatus.vlaue
        parameter["flyHeight"] = task.flySettingEntity.flyHeight.toString()
        parameter["speed"] = task.flySettingEntity.taskSpeed
//        parameter["rotatePitch"] = java.lang.String.valueOf(mSettingInfoEntity.getFlySetting().getCameraAngle())
        parameter["wayPointVOList"] = getNewPoint(task.wayPointList)
        val json: String = GsonUtils.toJson(parameter)
        val requestBody = RequestBody.create(MediaType.parse("application/json"), json)
        apiService.updateMissionState(requestBody, authorization)
    }

    private fun getNewPoint(point: List<LatLng>): List<WayPoint> {
        val result: ArrayList<WayPoint> = ArrayList()
        for (i in point.indices) {
            val temp = WayPoint("", point[i].latitude, point[i].longitude, i + 1)
            result.add(temp)
        }
        return result
    }

    /**
     * 设置无人机返航
     *
     */
    fun startGoHome() {
        DJIDeviceHelper.instance.startGoHome {
            it?.let {
                ToastUtils.showLong("无人机返航错误:${it.description}")
            } ?: let {
                ToastUtils.showLong("开始返航")
            }
        }
    }

    /**
     * 恢复或暂停任务
     *
     */
    fun resumeOrStopMission() {
        MissionControl.getInstance()?.waypointMissionOperator?.let {
            when (it.currentState) {
                WaypointMissionState.EXECUTING -> {//正在执行  需要暂停

                }
                WaypointMissionState.EXECUTION_PAUSED -> {//正在暂停  需要继续

                }
            }
        }
    }

    /**
     * 暂停任务
     *
     */
    private fun pauseWaypointMission() {

    }

    /**
     * 恢复任务
     *
     */
    private fun resumeWaypointMission() {

    }

}