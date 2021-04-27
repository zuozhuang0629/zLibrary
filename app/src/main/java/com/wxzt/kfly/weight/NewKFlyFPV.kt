package com.wxzt.kfly.weight

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.blankj.utilcode.util.ColorUtils
import com.wxzt.kfly.R

import com.wxzt.kfly.appViewModel
import com.wxzt.kfly.dji.DJILiveHelper
import com.wxzt.kfly.enums.LiveStatusEnum

import com.wxzt.lib_common.ext.visibility
import dji.ux.widget.FPVWidget


import java.util.*


/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/12/30 13:27
 * Description:
 * History:
 */
class NewKFlyFPV @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private var fpvWidget: FPVWidget? = null
    private lateinit var tvLiveInfo: TextView
    private var mTimer: Timer? = null
    private var mTimerTask: TimerTask? = null


    var offsetX = 0f
    var offsetY = 0f
    private var isMax = false

    init {
        initUI(context)
    }

    private fun initUI(context: Context) {
        this.setBackgroundColor(R.color.color_333333)
        createLiveInfo(context)
        createFpv()
    }

    private fun initTimer() {
        mTimer = Timer()

        mTimerTask = object : TimerTask() {
            override fun run() {
                DJILiveHelper.instacne.let {
                    tvLiveInfo.post {
                        tvLiveInfo.text = String.format("直播中: fps:%.2f", it.liveVideoFps)
                    }
                }
            }
        }


    }

    private fun createLiveInfo(context: Context) {
        tvLiveInfo = TextView(context)
        tvLiveInfo.textSize = 12f
        tvLiveInfo.setTextColor(ColorUtils.getColor(R.color.lib_common_white))
        tvLiveInfo.maxLines = 1
        tvLiveInfo.setBackgroundColor(ColorUtils.getColor(R.color.lib_common_gray))
        tvLiveInfo.text = "直播状态"
        var layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.RIGHT
        tvLiveInfo.layoutParams = layoutParams
        tvLiveInfo.visibility(false)
        addView(tvLiveInfo)

    }

    /**
     * 创建fpv
     */
    private fun createFpv() {
        fpvWidget = FPVWidget(this.context)
//        fpvWidget!!.setSourceCameraNameVisibility(false)
//        fpvWidget!!.sets(false)

        this.addView(fpvWidget)
        val imageView = ImageView(context)
        imageView.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_add_box_24))
        imageView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        imageView.setOnClickListener { v ->
            val width: Int
            val height: Int
            val rate = 2f
            val imageView = v as ImageView
            if (isMax) {
                isMax = false
                width = (fpvWidget!!.width / rate).toInt()
                height = (fpvWidget!!.height / rate).toInt()
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_add_box_24))
            } else {
                isMax = true
                width = (fpvWidget!!.width * rate).toInt()
                height = (fpvWidget!!.height * rate).toInt()
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_indeterminate_check_box_24))
            }
            val params = layoutParams
            params.width = width + 50
            params.height = height
            layoutParams = params

            val paramsFpvWidget = fpvWidget!!.layoutParams
            paramsFpvWidget.width = width
            paramsFpvWidget.height = height
            fpvWidget!!.layoutParams = paramsFpvWidget
        }

        addView(imageView)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            KeyEvent.ACTION_DOWN -> {
                val location = IntArray(2)
                val p = this@NewKFlyFPV.parent as View
                p.getLocationOnScreen(location)
                offsetX = location[0] + event.x
                offsetY = location[1] + event.y
            }
            MotionEvent.ACTION_MOVE -> {
                this@NewKFlyFPV.x = event.rawX - offsetX
                this@NewKFlyFPV.y = event.rawY - offsetY
                println(event.rawY)
            }
            KeyEvent.ACTION_UP -> {
            }
        }
        return true
    }

    fun showLiveInfo(liveType: LiveStatusEnum) {
        tvLiveInfo.bringToFront()

        when (liveType) {
            LiveStatusEnum.LIVE_SUCCESS -> tvLiveInfo.post {
                startShowFPS()
            }
            LiveStatusEnum.LIVE_CLOSE -> {
                tvLiveInfo.post { stopShowFPS() }
            }
            LiveStatusEnum.LIVE_FAIL -> {
                tvLiveInfo.post { tvLiveInfo.text = "直播失败" }
            }
        }


    }

    private fun startShowFPS() {
        initTimer()
        tvLiveInfo.visibility(true)
        mTimer?.schedule(mTimerTask, 0, 1000)
    }

    private fun stopShowFPS() {
        tvLiveInfo.visibility(false)
        mTimer?.cancel()
    }
}