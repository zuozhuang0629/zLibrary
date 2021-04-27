package com.wxzt.kfly.dji

import com.wxzt.kfly.dji.ICommonCallBack.ICommonCallBackWith
import com.wxzt.kfly.dji.ICommonCallBack.ICommonResultCallBackWith
import dji.common.camera.SettingsDefinitions
import dji.common.error.DJIError
import dji.common.util.CommonCallbacks
import dji.sdk.camera.Camera

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/12 13:32
 * Description: dji相机帮助
 * History:
 */
class DJICameraHelper {

    fun switchShootModel() {
        switchCameraModel(SettingsDefinitions.CameraMode.SHOOT_PHOTO)
    }

    fun switchVideoModel() {
        switchCameraModel(SettingsDefinitions.CameraMode.RECORD_VIDEO)
    }

    fun switchCameraModel(cameraMode: SettingsDefinitions.CameraMode?) {
        val djiDeviceHelper: DJIDeviceHelper = DJIDeviceHelper.instance
        if (!djiDeviceHelper.isConnected) {
            return
        }
        val camera = djiDeviceHelper.productInstance?.camera
        camera?.setMode(cameraMode!!) { djiError: DJIError? ->
            if (null != djiError) {
                DJIErrorHelper.instance?.showError(djiError)
            }
        }
    }

    fun switchCameraModel(cameraMode: SettingsDefinitions.CameraMode?, commonCallBack: ICommonResultCallBackWith<String>) {
        val djiDeviceHelper: DJIDeviceHelper = DJIDeviceHelper.instance
        if (!djiDeviceHelper.isConnected) {
            return
        }
        val camera = djiDeviceHelper.productInstance?.camera
        camera?.setMode(cameraMode!!) { djiError: DJIError? ->
            if (null != djiError) {
                commonCallBack.onFailure(djiError)
            } else {
                commonCallBack.onSuccess(null)
            }
        }
    }

    /**
     * 设置相机为自动曝光
     */
    fun SetCameraAuto() {

        val djiDeviceHelper: DJIDeviceHelper = DJIDeviceHelper.instance
        if (!djiDeviceHelper.isConnected) {
            return
        }
        val camera = djiDeviceHelper.productInstance?.camera
        camera?.setAELock(false) { djiError ->

        }
    }

    val camera: Camera?
        get() = if (!DJIDeviceHelper.instance.isConnected) {
            null
        } else DJIDeviceHelper.instance.productInstance?.camera

    /**
     * 拍摄照片 带toast
     */
    fun shootPhotoToast() {
        val camera = camera ?: return
        camera.startShootPhoto {

        }
    }

    /**
     * 设置文件自定义信息
     */
    fun setMediaFileCustomInformation(info: String?) {
        val camera = camera ?: return
        camera.setMediaFileCustomInformation(info!!, null)
    }

    /**
     * 拍摄照片
     */
    fun shootPhoto() {
        val camera = camera ?: return
        camera.startShootPhoto { djiError: DJIError? -> }
    }

    fun setPhotoAspectRatio(photoAspectRatio: SettingsDefinitions.PhotoAspectRatio?,
                            callBack: ICommonCallBackWith<DJIError?>?) {
        val camera = camera ?: return
        camera.setPhotoAspectRatio(photoAspectRatio!!){
            if (null == callBack) {
                return@setPhotoAspectRatio
            }
            callBack.onCallBack(it)
            //                ToastUtil.showToast("设置照片宽高比失败：" + djiError.toString());
        }
    }

    fun setPhotoFileFormat(photoFileFormat: SettingsDefinitions.PhotoFileFormat?,
                           callBack: ICommonCallBackWith<DJIError?>?) {
        val camera = camera ?: return
        camera.setPhotoFileFormat(photoFileFormat!!) { djiError ->
            if (null == callBack) {
                return@setPhotoFileFormat
            }
            callBack.onCallBack(djiError)
            //                ToastUtil.showToast("设置照片格式失败：" + djiError.toString());
        }
    }

    companion object {
        private var mInstance: DJICameraHelper? = null
        val instance: DJICameraHelper?
            get() {
                synchronized(DJICameraHelper::class.java) {
                    if (null == mInstance) {
                        mInstance = DJICameraHelper()
                    }
                }
                return mInstance
            }
    }
}