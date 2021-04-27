package com.wxzt.kfly.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wxzt.kfly.R
import com.wxzt.kfly.base.BaseActivity
import com.wxzt.kfly.databinding.ActivityTaskLocationDetailsBinding

class TaskLocationDetails : BaseActivity<ActivityTaskLocationDetailsBinding>() {
    override fun getViewBinding(): ActivityTaskLocationDetailsBinding {
        return ActivityTaskLocationDetailsBinding.inflate(layoutInflater)

    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    override fun initListener() {
    }

}