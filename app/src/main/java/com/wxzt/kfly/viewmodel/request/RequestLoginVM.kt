package com.wxzt.kfly.viewmodel.request

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.wxzt.kfly.entitys.UserInfo
import com.wxzt.kfly.network.apiService
import com.wxzt.lib_common.databind.BooleanObservableField
import com.wxzt.lib_common.databind.StringObservableField


import com.wxzt.lib_common.viewmodel.BaseViewModel
import com.wxzt.lib_common.ext.request
import com.wxzt.lib_common.network.state.ResultState
import okhttp3.ResponseBody


/**
 *  author : zuoz
 *  date : 2021/4/1 16:11
 *  description :
 */
class RequestLoginVM : BaseViewModel() {

    //用户名
    var username = StringObservableField()

    //密码(登录)
    var password = StringObservableField()

    //是否显示明文密码（登录注册界面）
    var isShowPwd = BooleanObservableField()


    //用户名清除按钮是否显示   不要在 xml 中写逻辑 所以逻辑判断放在这里
    var clearVisible = object : ObservableInt(username) {
        override fun get(): Int {
            return if (username.get().isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    //密码显示按钮是否显示   不要在 xml 中写逻辑 所以逻辑判断放在这里
    var passwordVisible = object : ObservableInt(password) {
        override fun get(): Int {
            return if (password.get().isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    var loginResult = MutableLiveData<ResultState<UserInfo>>()


    fun login(userName: String, pwd: String) {

        request({ apiService.getUserInfo(userName, pwd, "password") }, loginResult,isShowDialog = true)
    }
}