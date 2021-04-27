package com.wxzt.kfly.ui.activity

import android.os.Bundle
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.wxzt.kfly.appViewModel

import com.wxzt.kfly.base.BaseActivity
import com.wxzt.kfly.databinding.ActivityLoginBinding
import com.wxzt.kfly.ext.showMessage
import com.wxzt.kfly.ustils.CacheUtil
import com.wxzt.kfly.viewmodel.request.RequestLoginVM
import com.wxzt.lib_common.ext.parseState


class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val mViewModel: RequestLoginVM by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(RequestLoginVM::class.java)
    }

    override fun getViewBinding(): ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.click = ProxyClick()
        mViewBinding.vm = mViewModel
    }

    override fun initData() {
    }

    override fun initListener() {
        mViewBinding.imgBack.setOnClickListener { this.finish() }

        mViewModel.loginResult.observe(this) { resultState ->
            parseState(resultState, {
                it.userName = mViewBinding.edtUsername.text.toString()
                CacheUtil.setUserInfo(it)
                appViewModel.userInfo.value = it
                this.finish()
            }, {
                ToastUtils.showLong(it.getValue())
            })
        }
    }


    inner class ProxyClick {

        fun clear() {
            mViewModel.username.set("")
        }

        fun login() {
            when {
                mViewModel.username.get().isEmpty() -> showMessage("请填写账号")
                mViewModel.password.get().isEmpty() -> showMessage("请填写密码")
                else -> mViewModel.login(
                        mViewModel.username.get(),
                        mViewModel.password.get()
                )
            }
        }


        var onCheckedChangeListener =
                CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                    mViewModel.isShowPwd.set(isChecked)
                }
    }

}