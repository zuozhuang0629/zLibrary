package com.wxzt.lib_common.enums;

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2020/4/23 17:29
 * Description:  提供服务器使用
 * History:
 */
public enum FeatureType {
    //1.倾斜采集，2.多边形采集，3.指点采集 4.全景
    POLYGON("polygon"),
    POINT("point"),
    LINE("line");

    private String type;

    FeatureType(String type) {
        this.type = type;
    }

    //自定义方法
    public String getType() {
        return type;
    }
}
