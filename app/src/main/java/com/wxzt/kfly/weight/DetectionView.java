package com.wxzt.kfly.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import com.wxzt.kfly.R;
import com.wxzt.lib_common.enums.DetectionViewType;
import com.wxzt.lib_common.enums.LoadStatusEnum;
import com.wxzt.kfly.dji.ICommonCallBack;


/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/11 17:45
 * Description: 检测view
 * History:
 */
public class DetectionView extends LinearLayout {
    private LoadView ivState;
    private TextView tvInfo;

    private LinearLayout llStatus;

    private int loadFailureColor;
    private int loadSuccessColor;
    private int loadDefColor;
    private ICommonCallBack.ICommonCallBackWith<String> mOnClick;


    public DetectionView(Context context) {
        this(context, null);
    }

    public DetectionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_detection, this);
        this.ivState = view.findViewById(R.id.lib_common_iv_state);
        this.tvInfo = view.findViewById(R.id.lib_common_tv_info);
        this.llStatus = view.findViewById(R.id.lib_common_ll_status);


        //跑马灯效果必须加
        this.tvInfo.setSelected(true);
        llStatus.setVisibility(VISIBLE);

        loadFailureColor = ContextCompat.getColor(context, R.color.lib_common_orange);
        loadDefColor = Color.GRAY;
        loadSuccessColor = ContextCompat.getColor(context, R.color.lib_common_theme_green);
        initParams(context, attrs);
    }

    /**
     * 初始化属性
     */
    private void initParams(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.lib_common_DetectionView);

        if (null == typedArray) {
            return;
        }
        try {
            String text = typedArray.getString(R.styleable.lib_common_DetectionView_lib_common_detection_info);
            this.tvInfo.setText(text);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            typedArray.recycle();
        }
    }

    public DetectionViewType currentState = DetectionViewType.NORMAL;


    public void setState(DetectionViewType detectionViewType, String info) {
        this.tvInfo.post(new Runnable() {
            @Override
            public void run() {
                tvInfo.setText(info);
            }
        });
        LoadStatusEnum current = ivState.getmStatus();
        switch (detectionViewType) {
            case OK:
                if (current == LoadStatusEnum.LoadSuccess) {
                    return;
                }
                tvInfo.setTextColor(loadSuccessColor);
                ivState.loadSuccess();
                break;
            case NORMAL:
                if (current == LoadStatusEnum.Loading) {
                    return;
                }
                tvInfo.setTextColor(loadDefColor);
                ivState.loadLoading();
                break;
            case WARRAY:
                if (current == LoadStatusEnum.LoadFailure) {
                    return;
                }
                tvInfo.setTextColor(loadFailureColor);
                ivState.loadFailure();
                break;
        }

        currentState = detectionViewType;

    }


    public void setCreateBtnListener(ICommonCallBack.ICommonCallBackWith<String> onClick) {
        mOnClick = onClick;
    }

}
