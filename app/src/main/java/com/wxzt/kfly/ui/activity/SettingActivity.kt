package com.wxzt.kfly.ui.activity

import android.os.Bundle
import android.widget.TextView
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.getCustomView
import com.wxzt.kfly.R
import com.wxzt.kfly.appViewModel
import com.wxzt.kfly.base.BaseActivity
import com.wxzt.kfly.databinding.ActivitySettingBinding
import com.wxzt.kfly.ext.showCustomDialog
import com.wxzt.kfly.network.api.Api
import com.wxzt.kfly.ustils.CacheUtil
import me.jessyan.retrofiturlmanager.RetrofitUrlManager

class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    override fun getViewBinding(): ActivitySettingBinding = ActivitySettingBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.click = ProxyClick()
    }

    override fun initData() {
        mViewBinding.httpConfigTv.text = appViewModel.httpConfigInfo.value
    }

    override fun initListener() {
        appViewModel.httpConfigInfo.observe(this) {
            CacheUtil.setHttpConfig(it)
            when (it) {
                Api.BASE_TEST_NAME -> {
                    mViewBinding.httpConfigTv.text = "测试环境"
                }

                Api.BASE_PRO_NAME -> {
                    mViewBinding.httpConfigTv.text = "生产环境"
                }

                Api.BASE_DEV_NAME -> {
                    mViewBinding.httpConfigTv.text = "发布环境"
                }
            }

            RetrofitUrlManager.getInstance().setGlobalDomain(Api.getAddress(it))
        }
    }


    inner class ProxyClick {

        fun showHttpConfigDialog() {
            val dialog = showCustomDialog(BottomSheet(LayoutMode.WRAP_CONTENT), R.layout.dialog_http_config) { }
            val view = dialog.getCustomView()
            val cancel = view.findViewById<TextView>(R.id.dialog_cancel)
            val produce = view.findViewById<TextView>(R.id.dialog_produce_http)
            val release = view.findViewById<TextView>(R.id.dialog_release_http)
            val debug = view.findViewById<TextView>(R.id.dialog_debug_http)

            cancel.setOnClickListener {
                dialog.dismiss()
            }

            produce.setOnClickListener {
                appViewModel.httpConfigInfo.value = Api.BASE_PRO_NAME
            }

            release.setOnClickListener {
                appViewModel.httpConfigInfo.value = Api.BASE_DEV_NAME
            }

            debug.setOnClickListener {
                appViewModel.httpConfigInfo.value = Api.BASE_TEST_NAME
            }
        }


    }

}