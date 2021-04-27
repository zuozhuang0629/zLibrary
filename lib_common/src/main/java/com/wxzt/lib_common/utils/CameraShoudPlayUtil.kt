package com.wxzt.lib_common.utils

import android.media.AudioManager
import android.media.SoundPool
import com.wxzt.lib_common.R

import com.wxzt.lib_common.viewmodel.appContext

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/21 17:58
 * Description: 拍照音频工具
 * History:
 */
class CameraShoudPlayUtil {
    // SoundPool对象
    private val mSoundPlayer: SoundPool = SoundPool(10,
            AudioManager.STREAM_SYSTEM, 5)

    /**
     * 播放声音
     *
     */
    fun play() {
        mSoundPlayer.play(1, 1f, 1f, 0, 0, 1f)
    }

    companion object {
        private var mInstance: CameraShoudPlayUtil? = null

        // 1
        val instance: CameraShoudPlayUtil
            get() {
                if (null == mInstance) {
                    mInstance = CameraShoudPlayUtil()
                    mInstance!!.mSoundPlayer.load(appContext, R.raw.shutter_1, 1) // 1
                }
                return mInstance!!
            }
    }
}