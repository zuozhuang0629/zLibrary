package com.wxzt.kfly.ui.dialog

import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.aigestudio.wheelpicker.WheelPicker
import com.blankj.utilcode.util.ToastUtils
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import com.wxzt.kfly.R
import com.wxzt.kfly.databinding.DialogSettingBinding
import com.wxzt.kfly.db.entity.FlySettingEntity
import com.wxzt.kfly.enums.TaskModelEnum
import com.wxzt.kfly.interfaces.ISettingChange
import com.wyzt.lib_common.base.dialog.BaseVBDialog

/**
 *  author      : zuoz
 *  date        : 2021/4/7 16:24
 *  description :设置dialog
 */
class SettingDialog(private val flySettingEntity: FlySettingEntity?, val change: ISettingChange) :
        BaseVBDialog<DialogSettingBinding>(), WheelPicker.OnItemSelectedListener, OnSeekChangeListener {

    var lastHeight: Int = 100
    var lastTaskSpeed: Int = 5
    var lastTakeOffSpeed: Int = 10

    override fun getLayoutId(): Int = R.layout.dialog_setting

    override fun setDialogGravity(): Int = Gravity.RIGHT

    override fun getViewBinding(): DialogSettingBinding {
        return DialogSettingBinding.inflate(layoutInflater)
    }

    override fun setDialogSize(wlp: WindowManager.LayoutParams, window: Window) {
        val wm = context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)

        wlp.width = (dm.widthPixels * 0.4).toInt()
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT
//        wlp.verticalMargin = 0.1f
        window.attributes = wlp


    }

    override fun getAnim(): Int = R.style.planning_setting

    override fun getIsOnTouchOutside(): Boolean = true

    override fun initView() {
        initWheelView()
        initData()
    }

    private fun initWheelView() {
        val modelItems = listOf(TaskModelEnum.POLYGON.getName(), TaskModelEnum.OBLIQUE.getName(), TaskModelEnum.FIX.getName(),
                TaskModelEnum.PANORAMIC.getName())
        mViewBinding.planningModel.data = modelItems

        val hundred = listOf(0, 1, 2, 3, 4, 5)
        val items = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)

        mViewBinding.planning3.data = hundred
        mViewBinding.planning1.data = items
        mViewBinding.planning2.data = items

        mViewBinding.planning3.setSelectedItemPosition(1, false)

        val speed1 = listOf(0, 1)
        mViewBinding.planningTakeOffSpeed2.data = speed1
        mViewBinding.planningTakeOffSpeed1.data = items
        mViewBinding.planningTakeOffSpeed1.setSelectedItemPosition(1, false)

        mViewBinding.planningTaskSpeed.data = speed1
        mViewBinding.planningTaskSpeed.setSelectedItemPosition(1, false)
        mViewBinding.planningTaskSpeed2.data = items

        mViewBinding.planningModel.setOnItemSelectedListener(this)
        mViewBinding.planning3.setOnItemSelectedListener(this)
        mViewBinding.planning2.setOnItemSelectedListener(this)
        mViewBinding.planning1.setOnItemSelectedListener(this)
        mViewBinding.planningTakeOffSpeed1.setOnItemSelectedListener(this)
        mViewBinding.planningTakeOffSpeed2.setOnItemSelectedListener(this)
        mViewBinding.planningTaskSpeed.setOnItemSelectedListener(this)
        mViewBinding.planningTaskSpeed2.setOnItemSelectedListener(this)

        mViewBinding.planningHorizontalSeek.onSeekChangeListener = this
        mViewBinding.planningVerticalSeek.onSeekChangeListener = this
        mViewBinding.planningCompleteSeek.onSeekChangeListener = this

    }

    private fun initData() {
        if (flySettingEntity == null) {
            return
        }
        when (flySettingEntity.taskModel) {
            TaskModelEnum.POLYGON -> {
                mViewBinding.planningModel.setSelectedItemPosition(0, false)
            }
            TaskModelEnum.OBLIQUE -> {
                mViewBinding.planningModel.setSelectedItemPosition(1, false)
            }
            TaskModelEnum.PANORAMIC -> {
                mViewBinding.planningModel.setSelectedItemPosition(3, false)
            }
            TaskModelEnum.FIX -> {
                mViewBinding.planningModel.setSelectedItemPosition(2, false)
            }
        }

        val height3 = flySettingEntity.flyHeight / 100
        val height2 = flySettingEntity.flyHeight / 10 % 10
        val height1 = flySettingEntity.flyHeight % 10
        mViewBinding.planning3.setSelectedItemPosition(height3, false)
        mViewBinding.planning2.setSelectedItemPosition(height2, false)
        mViewBinding.planning1.setSelectedItemPosition(height1, false)

        val takeOffSpeed2 = flySettingEntity.takeOffSpeed / 10 % 10
        val takeOffSpeed1 = flySettingEntity.takeOffSpeed % 10
        mViewBinding.planningTakeOffSpeed2.setSelectedItemPosition(takeOffSpeed2, false)
        mViewBinding.planningTakeOffSpeed1.setSelectedItemPosition(takeOffSpeed1, false)

    }

    override fun onItemSelected(picker: WheelPicker, data: Any, position: Int) {
        when (picker.id) {
            R.id.planning_model -> {
                mViewBinding.planningModel.setSelectedItemPosition(position, false)
                flySettingEntity?.taskModel = TaskModelEnum.getModelEnum(data as String)
                change.setDataChanged(flySettingEntity)
            }
            R.id.planning_1 -> {
                mViewBinding.planning1.setSelectedItemPosition(position, false)
                if (checkFlyHeight()) {
                    flySettingEntity?.flyHeight = lastHeight
                    change.setDataChanged(flySettingEntity)
                }
            }
            R.id.planning_2 -> {
                mViewBinding.planning2.setSelectedItemPosition(position, false)
                if (checkFlyHeight()) {
                    flySettingEntity?.flyHeight = lastHeight
                    change.setDataChanged(flySettingEntity)
                }
            }
            R.id.planning_3 -> {
                mViewBinding.planning3.setSelectedItemPosition(position, false)
                if (checkFlyHeight()) {
                    flySettingEntity?.flyHeight = lastHeight
                    change.setDataChanged(flySettingEntity)
                }
            }
            R.id.planning_take_off_speed2 -> {
                mViewBinding.planningTakeOffSpeed2.setSelectedItemPosition(position, false)
                if (checkTakeOffHeight()) {
                    flySettingEntity?.takeOffSpeed = lastTakeOffSpeed
                    change.setDataChanged(flySettingEntity)
                }
            }
            R.id.planning_take_off_speed1 -> {
                mViewBinding.planningTakeOffSpeed1.setSelectedItemPosition(position, false)
                if (checkTakeOffHeight()) {
                    flySettingEntity?.takeOffSpeed = lastTakeOffSpeed
                    change.setDataChanged(flySettingEntity)
                }
            }
            R.id.planning_task_speed -> {
                mViewBinding.planningTaskSpeed.setSelectedItemPosition(position, false)
                if (checkTaskHeight()) {
                    flySettingEntity?.taskSpeed = lastTaskSpeed
                    change.setDataChanged(flySettingEntity)
                }
            }
            R.id.planning_task_speed2 -> {
                mViewBinding.planningTaskSpeed2.setSelectedItemPosition(position, false)
                if (checkTaskHeight()) {
                    flySettingEntity?.taskSpeed = lastTaskSpeed
                    change.setDataChanged(flySettingEntity)
                }
            }
        }
    }

    private fun checkFlyHeight(): Boolean {
        val height3 = mViewBinding.planning3.data[mViewBinding.planning3.selectedItemPosition] as Int
        val height2 = mViewBinding.planning3.data[mViewBinding.planning2.selectedItemPosition] as Int
        val height1 = mViewBinding.planning3.data[mViewBinding.planning1.selectedItemPosition] as Int
        val height = height3 * 100 + height2 * 10 + height1

        if (height < 20 || height > 500) {
            ToastUtils.showLong("飞行高度请在20~500m选择")
            mViewBinding.planning3.setSelectedItemPosition(lastHeight / 100, false)
            mViewBinding.planning2.setSelectedItemPosition(lastHeight / 10 % 10, false)
            mViewBinding.planning1.setSelectedItemPosition(lastHeight % 10, false)
            return false
        }

        lastHeight = height
        return true
    }


    private fun checkTaskHeight(): Boolean {
        val height2 = mViewBinding.planningTaskSpeed2.data[mViewBinding.planningTaskSpeed2.selectedItemPosition] as Int
        val height1 = mViewBinding.planningTaskSpeed.data[mViewBinding.planningTaskSpeed.selectedItemPosition] as Int
        val height = height2 * 10 + height1

        if (height > 20 || height < 3) {
            ToastUtils.showLong("任务速度请在3~20m/s选择")
            mViewBinding.planningTaskSpeed2.setSelectedItemPosition(lastHeight / 10 % 10, false)
            mViewBinding.planningTaskSpeed.setSelectedItemPosition(lastHeight % 10, false)
            return false
        }

        lastTaskSpeed = height
        return true
    }

    private fun checkTakeOffHeight(): Boolean {
        val height2 = mViewBinding.planningTakeOffSpeed2.data[mViewBinding.planningTakeOffSpeed2.selectedItemPosition] as Int
        val height1 = mViewBinding.planningTakeOffSpeed1.data[mViewBinding.planningTakeOffSpeed1.selectedItemPosition] as Int
        val height = height2 * 10 + height1

        if (height > 20 || height < 3) {
            ToastUtils.showLong("起飞速度请在3~20m/s选择")
            mViewBinding.planningTakeOffSpeed2.setSelectedItemPosition(lastHeight / 10 % 10, false)
            mViewBinding.planningTakeOffSpeed1.setSelectedItemPosition(lastHeight % 10, false)
            return false
        }

        lastTakeOffSpeed = height
        return true
    }

    override fun onSeeking(seekParams: SeekParams) {
        when (seekParams.seekBar.id) {
            R.id.planning_horizontal_seek -> {
                mViewBinding.planningHorizontalTv.text = "${seekParams.seekBar.progress}%"
            }
            R.id.planning_vertical_seek -> {
                mViewBinding.planningVerticalTv.text = "${seekParams.seekBar.progress}%"

            }
            R.id.planning_complete_seek -> {
                mViewBinding.planningCompleteTv.text = "${seekParams.seekBar.progress}%"
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: IndicatorSeekBar) {
        when (seekBar.id) {
            R.id.planning_horizontal_seek -> {
                flySettingEntity?.horizontalOverlap = seekBar.progress * 1.0f / 100
                change.setDataChanged(flySettingEntity)
            }
            R.id.planning_vertical_seek -> {
                flySettingEntity?.verticalOverlap = seekBar.progress * 1.0f / 100
                change.setDataChanged(flySettingEntity)
            }
            R.id.planning_complete_seek -> {
                flySettingEntity?.taskComplete = seekBar.progress
                change.setDataChanged(flySettingEntity)

            }
        }
    }


}