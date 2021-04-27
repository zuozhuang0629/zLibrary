package com.wxzt.lib_common.event;

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/4
 * Description: dji 注册消息
 */
public class DJIRegisterEven {
    private RegisterType type;

    public DJIRegisterEven(RegisterType msg) {
        type = msg;
    }

    public RegisterType getMsg() {
        return type;
    }

    public enum RegisterType {
        REGISTER_SUCCESS,
        REGISTER_ERROR,
        PRODUCT_DISCONNECT,
        PRODUCT_CONNECT,
        COMPONENT_CHANGE;
    }
}
