package com.wxzt.kfly.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.wxzt.lib_common.ext.startActivity


import com.wxzt.kfly.R
import com.wxzt.kfly.appViewModel

import com.wxzt.kfly.databinding.FragmentMeBinding
import com.wxzt.kfly.ui.activity.LoginActivity
import com.wxzt.kfly.ui.activity.SettingActivity
import com.wxzt.kfly.ustils.CacheUtil


import com.wxzt.kfly.viewmodel.MeVM
import com.wxzt.lib_common.base.fragment.BaseVBFragment

import com.wyzt.lib_common.ext.utils.notNull

/**
 *  author : zuoz
 *  date : 2021/3/29 8:54
 *  description :
 */
class MeFragment : BaseVBFragment<FragmentMeBinding>() {
    private val mViewModel: MeVM by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MeVM::class.java)
    }

    override fun getViewBinding(): FragmentMeBinding = FragmentMeBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        mViewBinding.vm = mViewModel
        mViewBinding.click = ProxyClick()
    }

    override fun initData() {
        appViewModel.userInfo.value?.let {
            mViewModel.name.set(it.userName)
            mViewModel.isLogin.set(true)
        }
    }

    override fun initListener() {
        appViewModel.userInfo.observe(this) {
            it.notNull({
                mViewModel.name.set(it.userName)
                mViewModel.isLogin.set(true)
            }, {
                mViewModel.name.set("请先登录~")
                mViewModel.isLogin.set(false)
            })

        }

    }

    inner class ProxyClick {
        fun gotoLogin() {
            requireContext().startActivity<LoginActivity>()
        }

        fun gotoSettingActivity() {
            requireContext().startActivity<SettingActivity>()
        }

        fun loginOut() {
          mViewModel.loginOut()
        }

    }
}