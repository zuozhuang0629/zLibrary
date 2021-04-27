package com.wxzt.kfly.dji

import com.wxzt.lib_common.interfaces.dji.IDJIBatteryInfo
import dji.common.battery.BatteryState

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/12 11:24
 * Description: 无人机电量帮助类
 * History:
 */
class DJIBatteryHelper {
    private val mDjiBatteryInfo: IDJIBatteryInfo? = null
    private val mBatteryState: BatteryState? = null

    /**
     * 电量是否满足起飞条件  目前是30%
     *
     * @return
     */
    val isCanFly: Boolean
        get() {
            if (!DJIDeviceHelper.instance.isConnected) {
                return false
            }
            return if (null == mBatteryState) {
                false
            } else mBatteryState.chargeRemainingInPercent > 30
        }

    /**
     * dji电量监听
     */
    fun setStateCallBack() {}

    companion object {
        private var mInstance: DJIBatteryHelper? = null
        val instance: DJIBatteryHelper?
            get() {
                if (null == mInstance) {
                    mInstance = DJIBatteryHelper()
                    mInstance!!.setStateCallBack()
                }
                return mInstance
            }
    }
}