package com.wxzt.kfly.dji;

import dji.common.error.DJIError;

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/21 15:30
 * Description: 一般会回调接口
 * History:
 */
public interface ICommonCallBack {
    public interface ICommonResultCallBackWith<T> {
        void onSuccess(T result);

        void onFailure(DJIError djiError);
    }

    public interface ICommonCallBackWith<T> {
        void onCallBack(T result);
    }
}
