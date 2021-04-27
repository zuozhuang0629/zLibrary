package com.wxzt.lib_common.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseVBActivity<VB : ViewBinding> : AppCompatActivity() {
    protected lateinit var mViewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = getViewBinding()
        setContentView(mViewBinding.root)
        initView(savedInstanceState)
        initListener()
        initData()
    }


    abstract fun getViewBinding(): VB

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     *
     */
    abstract fun initData()

    /**
     * 初始化监听
     *
     */
    abstract fun initListener()

    abstract fun showLoading(message: String)

    abstract fun dismissLoading()

}