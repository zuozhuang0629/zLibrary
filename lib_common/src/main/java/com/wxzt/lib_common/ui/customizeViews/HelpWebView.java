package com.wxzt.lib_common.ui.customizeViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/9/16
 * Description: app帮助界面显示webView
 */
public class HelpWebView extends WebView {
    public HelpWebView(Context context) {
        this(context, null);
    }

    public HelpWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HelpWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUrl() {
        this.loadUrl("file:///android_asset/index.html");
        this.getSettings().setJavaScriptEnabled(true);
        this.setWebViewClient(new WebViewClient());
    }

    public void showWeb() {
        this.setVisibility(VISIBLE);
        setUrl();
    }

    /**
     * 是否显示
     *
     * @return
     */
    public boolean isShow() {
        return this.getVisibility() == VISIBLE;
    }

    /**
     * 设置显隐
     *
     * @param visible
     */
    public void setVisible(int visible) {
        this.setVisibility(visible);
    }
}
