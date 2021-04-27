package com.wxzt.kfly.base

import androidx.viewbinding.ViewBinding
import com.wxzt.kfly.ext.dismissLoadingExt
import com.wxzt.kfly.ext.showLoadingExt
import com.wxzt.lib_common.base.activity.BaseVBActivity


/**
 *  author : zuoz
 *  date : 2021/4/1 17:02
 *  description :kfly 的base
 */
abstract class BaseActivity<VB : ViewBinding> : BaseVBActivity<VB>() {

    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    override fun dismissLoading() {
        dismissLoadingExt()
    }


}