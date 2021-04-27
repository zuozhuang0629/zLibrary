package com.wxzt.kfly.dji

import com.blankj.utilcode.util.ToastUtils
import dji.midware.data.model.P3.Fa
import dji.sdk.sdkmanager.DJISDKManager
import dji.sdk.sdkmanager.LiveStreamManager

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/11 15:52
 * Description: 大疆直播
 * History:
 */
class DJILiveHelper {
    /**
     * 开始直播
     *
     * @param url
     */
    fun startLiveShow(url: String?) {
        if (!isLiveStreamManagerOn) {
            return
        }
        if (DJISDKManager.getInstance().liveStreamManager.isStreaming) {
            return
        }
        object : Thread() {
            override fun run() {
                DJISDKManager.getInstance().liveStreamManager.liveUrl = url
                DJISDKManager.getInstance().liveStreamManager.startStream()
                DJISDKManager.getInstance().liveStreamManager.setStartTime()
            }
        }.start()
    }

    fun isLiving(): Boolean {
        if (!isLiveStreamManagerOn) {
            return false
        }

        return DJISDKManager.getInstance().liveStreamManager.isStreaming

    }

    /**
     * 停止直播
     */
    fun stopLive() {
        if (!isLiveStreamManagerOn) {
            return
        }
        DJISDKManager.getInstance().liveStreamManager.stopStream()
    }

    private val isLiveStreamManagerOn: Boolean
        private get() = DJISDKManager.getInstance().liveStreamManager != null

    /**
     * 设置直播状态监听
     */
    fun setLiveListener(liveListener: LiveStreamManager.OnLiveChangeListener) {
        if (isLiveStreamManagerOn) {
            liveStreamManager.registerListener(liveListener)
        }
    }

    /**
     * 移除直播监听
     */
    fun removeLiveListener(liveListener: LiveStreamManager.OnLiveChangeListener?) {
        if (isLiveStreamManagerOn) {
            liveStreamManager.unregisterListener(liveListener)
        }
    }

    /*
     * 获得直播管理对象
     *
     * @return
     */
    private val liveStreamManager: LiveStreamManager
        private get() = DJISDKManager.getInstance().liveStreamManager

    /**
     * 获得直播视频帧率
     */
    val liveVideoFps: Float
        get() = if (isLiveStreamManagerOn) {
            liveStreamManager.liveVideoFps
        } else 0.0f

    companion object {
        private var mInstance: DJILiveHelper? = null
        val instacne: DJILiveHelper
            get() {
                if (null == mInstance) {
                    mInstance = DJILiveHelper()
                }
                return mInstance!!
            }
    }
}