package com.wxzt.kfly.ui.dialog

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.wxzt.kfly.entitys.TaskOffCheckEntity
import com.wxzt.lib_common.viewmodel.BaseViewModel
import dji.common.camera.SettingsDefinitions
import dji.keysdk.*
import dji.keysdk.callback.KeyListener
import dji.ux.beta.core.base.DJISDKModel
import dji.ux.beta.core.base.WidgetModel
import dji.ux.beta.core.communication.ObservableInMemoryKeyedStore
import dji.ux.beta.core.util.DataProcessor


/**
 *  author      : zuoz
 *  date        : 2021/4/22 11:08
 *  description :
 */
private const val DEFAULT_PERCENTAGE = 0

class TaskOffCheckMV(
        djiSdkModel: DJISDKModel,
        keyedStore: ObservableInMemoryKeyedStore
) : WidgetModel(djiSdkModel, keyedStore) {

    val check: MutableLiveData<TaskOffCheckEntity> = MutableLiveData()

    private val productKeyProcessor = DataProcessor.create(DEFAULT_PERCENTAGE)
    private val batteryPercentKeyProcessor = DataProcessor.create(DEFAULT_PERCENTAGE)
    private val satelliteCountProcessor = DataProcessor.create(DEFAULT_PERCENTAGE)
    private val shootPhotoModedProcessor =
            DataProcessor.create(SettingsDefinitions.PhotoFileFormat.UNKNOWN)
    private val photoForMatProcessor =
            DataProcessor.create(SettingsDefinitions.ShootPhotoMode.UNKNOWN)

    private val sdStorageProcessor =
            DataProcessor.create(DEFAULT_PERCENTAGE)

    override fun inSetup() {
        //连接
        val productKey = ProductKey.create(ProductKey.CONNECTION)
        bindDataProcessor(productKey, productKeyProcessor)

        //电量变化
        val batteryPercentKey = BatteryKey.create(BatteryKey.CHARGE_REMAINING_IN_PERCENT, 0)
        bindDataProcessor(batteryPercentKey, batteryPercentKeyProcessor)

        //卫星
        val satelliteCount = FlightControllerKey.create(FlightControllerKey.SATELLITE_COUNT)
        bindDataProcessor(satelliteCount, satelliteCountProcessor)
        //相机模式
        val photoMode = CameraKey.create(CameraKey.SHOOT_PHOTO_MODE)
        bindDataProcessor(photoMode, shootPhotoModedProcessor)

        //相机格式
        val photoForMat = CameraKey.create(CameraKey.PHOTO_FILE_FORMAT)
        bindDataProcessor(photoForMat, photoForMatProcessor)

        //内存
        val sdStorage = CameraKey.create(CameraKey.SDCARD_REMAINING_SPACE_IN_MB)
        bindDataProcessor(sdStorage, sdStorageProcessor)

        //档位
//        val flightModeStringKey = FlightControllerKey.create(FlightControllerKey.FLIGHT_MODE_STRING)
//        bindDataProcessor(flightModeStringKey, flightModeStringProcessor)
    }

    override fun inCleanup() {
    }

    override fun updateStates() {
        if (productConnectionProcessor.value) {
            check.postValue(TaskOffCheckEntity(true,
                    batteryPercentKeyProcessor.value, satelliteCountProcessor.value,
                    photoForMatProcessor.value, shootPhotoModedProcessor.value,
                    sdStorageProcessor.value
            ))
        } else {
            check.postValue(TaskOffCheckEntity(false,
                    -1, -1,
                    SettingsDefinitions.ShootPhotoMode.UNKNOWN,
                    SettingsDefinitions.PhotoFileFormat.UNKNOWN,0
            ))
        }
    }


}