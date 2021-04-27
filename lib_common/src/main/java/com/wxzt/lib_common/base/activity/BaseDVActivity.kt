package com.wxzt.lib_common.base.activity

import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding


abstract class BaseDVActivity<VM : ViewModel, VB : ViewBinding> : BaseVBActivity<VB>() {
    protected abstract val mViewModel: VM
}