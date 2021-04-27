package com.wxzt.kfly.viewmodel
import com.wxzt.kfly.ustils.CacheUtil
import com.wxzt.kfly.ustils.DefDataUtil
import com.wxzt.lib_common.viewmodel.BaseViewModel
import com.wxzt.lib_common.databind.BooleanObservableField
import com.wxzt.lib_common.databind.StringObservableField

/**
 *  author : zuoz
 *  date : 2021/3/30 16:48
 *  description :
 */
class MeVM : BaseViewModel() {

    var name = StringObservableField("请先登录~")
    var imageUrl = StringObservableField(DefDataUtil.randomImage())
    var isLogin = BooleanObservableField(false)

    fun loginOut() {
        CacheUtil.loginOut()
        isLogin.set(false)
        name.set("请先登录~")
    }

}