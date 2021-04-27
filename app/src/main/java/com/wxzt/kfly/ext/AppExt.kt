package com.wxzt.kfly.ext

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import com.wxzt.kfly.appViewModel
import com.wxzt.kfly.entitys.UserInfo
import com.wxzt.kfly.ui.activity.LoginActivity
import com.wxzt.kfly.ustils.SettingUtil
import com.wxzt.lib_common.ext.startActivity


/**
 *  author : zuoz
 *  date : 2021/4/2 10:35
 *  description :
 */

/**
 * @param message 显示对话框的内容 必填项
 * @param title 显示对话框的标题 默认 温馨提示
 * @param positiveButtonText 确定按钮文字 默认确定
 * @param positiveAction 点击确定按钮触发的方法 默认空方法
 * @param negativeButtonText 取消按钮文字 默认空 不为空时显示该按钮
 * @param negativeAction 点击取消按钮触发的方法 默认空方法
 */
fun AppCompatActivity.showMessage(
        message: String,
        title: String = "温馨提示",
        positiveButtonText: String = "确定",
        positiveAction: () -> Unit = {},
        negativeButtonText: String = "",
        negativeAction: () -> Unit = {}
) {

    MaterialDialog(this)
            .cancelable(true)
            .lifecycleOwner(this)
            .show {
                title(text = title)
                message(text = message)
                positiveButton(text = positiveButtonText) {
                    positiveAction.invoke()
                }
                if (negativeButtonText.isNotEmpty()) {
                    negativeButton(text = negativeButtonText) {
                        negativeAction.invoke()
                    }
                }
                getActionButton(WhichButton.POSITIVE).updateTextColor(SettingUtil.getColor(this@showMessage))
                getActionButton(WhichButton.NEGATIVE).updateTextColor(SettingUtil.getColor(this@showMessage
                ))
            }

}

fun AppCompatActivity.isLoginAndStart(logined: (UserInfo) -> Unit) {
    appViewModel.userInfo.value?.let {
        logined(it)
    } ?: let {
        ToastUtils.showLong("请登录KFly账号")
        startActivity<LoginActivity>()
    }
}

fun Fragment.isLoginAndStart(logined: (UserInfo) -> Unit,noLogin:()->Unit) {
    appViewModel.userInfo.value?.let {
        logined(it)
    } ?: let {
        noLogin()
        ToastUtils.showLong("请登录KFly账号")
        requireActivity().startActivity<LoginActivity>()
    }
}