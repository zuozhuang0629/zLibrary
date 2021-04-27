package com.wxzt.lib_common.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


/**
 *  author : zuoz
 *  date : 2021/3/22 14:04
 *  description :
 */
abstract class BaseVBFragment<VB : ViewBinding> : Fragment() {
    protected lateinit var mViewBinding: VB
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mViewBinding = getViewBinding()
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
        initListener()
        initData()
    }

    abstract fun getViewBinding(): VB

    /**
     * 初始化view
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


}