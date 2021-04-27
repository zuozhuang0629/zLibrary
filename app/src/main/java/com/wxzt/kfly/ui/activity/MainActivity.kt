package com.wxzt.kfly.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.wxzt.kfly.R
import com.wxzt.kfly.appViewModel
import com.wxzt.kfly.base.BaseActivity
import com.wxzt.kfly.databinding.ActivityMainBinding
import com.wxzt.kfly.dji.DJIRegisterHelper
import com.wxzt.kfly.dji.DjiUserAccountHelper
import com.wxzt.kfly.enums.DjiProductStatus
import com.wxzt.kfly.network.api.Api
import com.wxzt.kfly.ui.fragment.MeFragment
import com.wxzt.kfly.ui.fragment.TaskManagementFragment
import com.wxzt.kfly.ui.fragment.UavFragment
import com.wxzt.kfly.ustils.CacheUtil
import com.wxzt.lib_common.event.DJIRegisterEven
import com.wxzt.lib_common.rx.RxBus
import me.jessyan.retrofiturlmanager.RetrofitUrlManager


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        initBottom()
        initViewPage()
    }

    override fun initData() {
        //DJI 注册
        RxBus.INSTANCE.doStickySubscribe(DJIRegisterEven::class.java) { djiRegisterEven ->
            when (djiRegisterEven.msg) {

            }
        }

//        //配置网络
//        appViewModel.httpConfigInfo.value?.let {
//            RetrofitUrlManager.getInstance().setGlobalDomain(Api.getAddress(it))
//        }
    }

    override fun initListener() {
        appViewModel.djiRegisterStatus.observe(this) {
            djiRegisterStatus(it)
        }

        DJIRegisterHelper.startSDKRegistration(this@MainActivity)
    }

    private fun initBottom() {
        mViewBinding.mainBottom.apply {
            enableAnimation(false)
            enableShiftingMode(false)
            enableItemShiftingMode(false)
            setIconSize(22.toFloat())
            setTextSize(11.toFloat())
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_uav -> mViewBinding.mainPage.setCurrentItem(0, false)
                    R.id.menu_task_management -> mViewBinding.mainPage.setCurrentItem(1, false)
                    R.id.menu_me -> mViewBinding.mainPage.setCurrentItem(2, false)
                }
                true
            }
        }

    }

    private fun initViewPage() {
        mViewBinding.mainPage.apply {
            //是否可滑动
            this.isUserInputEnabled = false
            this.offscreenPageLimit = 1
            this.adapter = object : FragmentStateAdapter(this@MainActivity) {
                override fun getItemCount(): Int = 3

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> {
                            UavFragment()
                        }
                        1 -> {
                            TaskManagementFragment()
                        }
                        2 -> {
                            MeFragment()
                        }
                        else -> UavFragment()
                    }
                }

            }
        }
    }

    private fun djiRegisterStatus(status: DjiProductStatus) {
        when (status) {
            DjiProductStatus.REGISTRATION_SUCCESS -> {
                ToastUtils.showLong("注册大疆SDK成功")
                DjiUserAccountHelper.instance.isLoginAndLogin(this@MainActivity, {
                    ToastUtils.showLong("登录大疆成功")
                    LogUtils.d(it)
                    CacheUtil.setIsLoginDji(true)
                }, {
                    CacheUtil.setIsLoginDji(false)
                    ToastUtils.showLong("登录大疆失败")

                })
            }
            DjiProductStatus.REGISTRATION_FAIL -> {
                ToastUtils.showLong("注册大疆sdk失败，请检查网络")
            }
            DjiProductStatus.PRODUCT_CONNECT -> {
                ToastUtils.showLong("连接无人机")
            }
            DjiProductStatus.PRODUCT_DISCONNECT -> {
                ToastUtils.showLong("断开无人机")
            }
        }

    }

}