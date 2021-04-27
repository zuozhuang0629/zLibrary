package com.wxzt.lib_common.event;

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/12 16:56
 * Description:
 * History:
 */
public class DJIAirLinkEven {
    private int signal;
    private AirLinkType type;

    public DJIAirLinkEven(AirLinkType airLinkType,int msg) {
        signal = msg;
        type = airLinkType;
    }

    public int getSignal() {
        return signal;
    }

    public AirLinkType getType() {
        return type;
    }

    public enum AirLinkType {
        UPLOAD,
        DOWNLOAD
    }

}
