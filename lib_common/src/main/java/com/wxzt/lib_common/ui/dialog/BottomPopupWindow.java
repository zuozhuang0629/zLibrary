package com.wxzt.lib_common.ui.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.wxzt.lib_common.R;

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2020/1/8 10:46
 * Description: 底部弹框
 * History:
 */
public class BottomPopupWindow {
    PopupWindow popupWindow;
    Context mContext;

    public BottomPopupWindow(Context context) {
        mContext = context;
        initUI();
    }

    private void initUI() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lib_common_popup_window_bottom, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.lib_common_animate_default_dialog);
        //设置位置
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        //设置消失监听
//        popupWindow.setOnDismissListener(this);
    }

    public void dismissionPopupWindow() {
        popupWindow.dismiss();
    }

}
