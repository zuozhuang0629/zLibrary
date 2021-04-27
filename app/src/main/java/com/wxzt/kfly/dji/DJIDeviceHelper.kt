package com.wxzt.kfly.dji

import com.wxzt.kfly.dji.ICommonCallBack.ICommonResultCallBackWith
import dji.common.error.DJIError
import dji.common.product.Model
import dji.common.util.CommonCallbacks
import dji.sdk.base.BaseProduct
import dji.sdk.camera.Camera
import dji.sdk.flightcontroller.FlightController
import dji.sdk.media.MediaManager
import dji.sdk.mission.MissionControl
import dji.sdk.products.Aircraft
import dji.sdk.sdkmanager.DJISDKManager

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/12 10:41
 * Description: 大疆帮助类   管理相关的
 * History:
 */
class DJIDeviceHelper {
    /**
     * 获取飞行控制器
     *
     * @return
     */
    val flightController: FlightController?
        get() = if (!isConnected) null else (productInstance as Aircraft?)!!.flightController

    /**
     * 获取飞行控制器
     *
     * @return
     */
    val remoteController: FlightController?
        get() = if (!isConnected) null else (productInstance as Aircraft?)!!.flightController

    /**
     * 确认无人机降落
     */
    fun setConfirmLanding() {
        val flightController = flightController ?: return
        flightController.confirmLanding { }
    }

    /**
     * dji是否连接
     *
     * @return
     */
    val isConnected: Boolean
        get() = productInstance != null && productInstance!!.isConnected

    /**
     * 获取设备单例
     *
     * @return
     */
    val productInstance: BaseProduct?
        get() {
            synchronized(DJIDeviceHelper::class.java) { return DJISDKManager.getInstance().product }
        }

    /**
     * 获取设备单例
     *
     * @return
     */
    val aircraftInstance: Aircraft?
        get() {
            val product = productInstance
            return if (product == null) null else product as Aircraft?
        }

    /**
     * 开始返航
     *
     * @param with
     */
    fun startGoHome(with: ((DJIError?) -> Unit)?) {
        if (!isConnected) {
            return
        }
        val aircraft = aircraftInstance ?: return
        val flightController = aircraft.flightController ?: return
        flightController.startGoHome { djiError ->
            with?.invoke(djiError)
        }
    }

    /**
     * 获取无人机主镜头
     *
     * @return
     */
    val mainCamera: Camera?
        get() {
            val aircraft = aircraftInstance ?: return null
            return aircraft.camera
        }

    /**
     * 获取媒体管理
     *
     * @return
     */
    val mediaManager: MediaManager?
        get() {
            val camera = mainCamera ?: return null
            return camera.mediaManager
        }



    /**
     * 是否支持下载
     *
     * @return
     */
    val isMediaDownloadModeSupported: Boolean
        get() {
            val camera = mainCamera ?: return false
            return camera.isMediaDownloadModeSupported
        }

    fun isMAVIC(model: Model?): Boolean {
        return when (model) {
            Model.MAVIC_2, Model.MAVIC_2_ENTERPRISE, Model.MAVIC_2_ENTERPRISE_DUAL, Model.MAVIC_2_PRO, Model.MAVIC_AIR, Model.MAVIC_PRO -> true
            else -> false
        }
    }

    companion object {
        private var mInstance: DJIDeviceHelper? = null
        val instance: DJIDeviceHelper
            get() {
                synchronized(DJIDeviceHelper::class.java) {
                    if (null == mInstance) {
                        mInstance = DJIDeviceHelper()
                    }
                }
                return mInstance!!
            }
    }
}