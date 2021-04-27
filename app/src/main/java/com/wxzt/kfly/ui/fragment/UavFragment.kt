package com.wxzt.kfly.ui.fragment

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.AppUtils
import com.wxzt.kfly.R
import com.wxzt.kfly.databinding.FragmentUavBinding
import com.wxzt.kfly.ext.isLoginAndStart
import com.wxzt.kfly.ui.activity.planning.PlanningActivity
import com.wxzt.lib_common.ext.startActivity

import com.wxzt.lib_common.base.fragment.BaseVBFragment


/**
 *  author : zuoz
 *  date : 2021/3/26 17:29
 *  description :
 */
class UavFragment : BaseVBFragment<FragmentUavBinding>(), View.OnClickListener {
    override fun getViewBinding(): FragmentUavBinding = FragmentUavBinding.inflate(layoutInflater)
    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
        mViewBinding.appName.text=  AppUtils.getAppName()
    }

    override fun initListener() {
        mViewBinding.ivOblique.setOnClickListener(this)
        mViewBinding.ivFree.setOnClickListener(this)

        isLoginAndStart({

        }, {

        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_oblique -> {
                requireContext().startActivity<PlanningActivity>()
            }


            R.id.iv_free -> {
            }


            else -> {
            }
        }

    }


}