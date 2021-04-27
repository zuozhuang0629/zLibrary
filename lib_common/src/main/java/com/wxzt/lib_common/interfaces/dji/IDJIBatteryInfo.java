package com.wxzt.lib_common.interfaces.dji;

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/12 13:17
 * Description:
 * History:
 */
public interface IDJIBatteryInfo {
    /**
     * 电量剩余百分比
     *
     * @param remaining
     */
    void batteryRemainingInPercent(int remaining);
}
