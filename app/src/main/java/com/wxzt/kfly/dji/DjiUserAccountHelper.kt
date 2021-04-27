package com.wxzt.kfly.dji

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import dji.common.error.DJIError
import dji.common.realname.AppActivationState
import dji.common.useraccount.UserAccountState
import dji.common.util.CommonCallbacks
import dji.common.util.CommonCallbacks.CompletionCallbackWith
import dji.sdk.realname.AppActivationManager
import dji.sdk.useraccount.UserAccountManager

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/14 16:26
 * Description: dji 登录
 * History:
 */
class DjiUserAccountHelper private constructor() {
    /**
     * 登录dji账号
     *
     * @param context
     */
    fun login(context: Context, success: (UserAccountState?) -> Unit, failure: () -> Unit) {
        UserAccountManager.getInstance()
                .logIntoDJIUserAccount(context, object : CompletionCallbackWith<UserAccountState?> {
                    override fun onSuccess(userAccountState: UserAccountState?) {
                        success(userAccountState)
                    }

                    override fun onFailure(djiError: DJIError) {
                        failure()
                    }
                })
    }

    fun loginOut() {
        UserAccountManager.getInstance().loginOut { }
    }

    /**
     * 是否登录
     */
    val isLogin: Boolean
        get() {
            val userAccountState = AppActivationManager.getInstance().appActivationState
            return userAccountState == AppActivationState.LOGIN_REQUIRED
        }

    /**
     * 判断是否登录，是否弹出登录框
     */
    fun isLoginAndLogin(context: Context, success: (UserAccountState?) -> Unit, failure: () -> Unit) {
        if (isLogin) {
            success.invoke(UserAccountManager.getInstance().userAccountState)
            return
        }
        login(context, success, failure)
    }

    companion object {
        private var mInstance: DjiUserAccountHelper? = null
        val instance: DjiUserAccountHelper
            get() {
                if (null == mInstance) {
                    mInstance = DjiUserAccountHelper()
                }
                return mInstance!!
            }
    }
}