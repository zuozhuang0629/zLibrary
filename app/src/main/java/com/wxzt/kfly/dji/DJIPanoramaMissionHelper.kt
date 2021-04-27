package com.wxzt.kfly.dji

import dji.common.camera.SettingsDefinitions
import dji.common.error.DJIError
import dji.common.mission.panorama.PanoramaMode
import dji.common.util.CommonCallbacks
import dji.sdk.mission.MissionControl

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2020/4/16 14:04
 * Description: dji全景帮助
 * History:
 */
class DJIPanoramaMissionHelper {
    fun setShootPhotoMode() {
        val missionOperator = MissionControl.getInstance().panoramaMissionOperator
                ?: return
        DJICameraHelper.instance?.camera?.setShootPhotoMode(SettingsDefinitions.ShootPhotoMode.PANORAMA,
                CommonCallbacks.CompletionCallback <DJIError>{
                    //                        ToastUtil.showToast("设置相机模式：" + djiError == null ? "成功" : djiError.toString());
                })
    }

    fun setUP() {
        val missionOperator = MissionControl.getInstance().panoramaMissionOperator
                ?: return
        missionOperator.setup(PanoramaMode.FULL_CIRCLE) {
            //                ToastUtil.showToast("setUP：" + djiError == null ? "成功" : djiError.toString());
        }
    }

    fun start() {
        val missionOperator = MissionControl.getInstance().panoramaMissionOperator
                ?: return
        missionOperator.startMission {
            //                ToastUtil.showToast("开始任务：" + djiError == null ? "成功" : djiError.toString());
        }
    }

    companion object {
        private var mInstance: DJIPanoramaMissionHelper? = null
        val instance: DJIPanoramaMissionHelper?
            get() {
                if (null == mInstance) {
                    mInstance = DJIPanoramaMissionHelper()
                }
                return mInstance
            }
    }
}