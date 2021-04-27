package com.wxzt.kfly.ext

import android.app.Activity
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.wxzt.kfly.R


/**
 *  author : zuoz
 *  date : 2021/3/31 9:31
 *  description :
 */

//loading框
private var loadingDialog: MaterialDialog? = null

/**
 * 打开等待框
 */
fun AppCompatActivity.showLoadingExt(message: String = "请求网络中") {
    if (!this.isFinishing) {
        if (loadingDialog == null) {
            loadingDialog = MaterialDialog(this)
                    .cancelable(true)
                    .cancelOnTouchOutside(false)
                    .cornerRadius(12f)
                    .customView(R.layout.dialog_loading)
                    .lifecycleOwner(this)
            loadingDialog?.getCustomView()?.run {
                this.findViewById<TextView>(R.id.loading_tips).text = message
//                this.findViewById<ProgressBar>(R.id.progressBar).indeterminateTintList = SettingUtil.getOneColorStateList(this@showLoadingExt)
            }
        }
        loadingDialog?.show()
    }
}

/**
 * 打开等待框
 */
fun Fragment.showLoadingExt(message: String = "请求网络中") {
    activity?.let {
        if (!it.isFinishing) {
            if (loadingDialog == null) {
                loadingDialog = MaterialDialog(it)
                        .cancelable(true)
                        .cancelOnTouchOutside(false)
                        .cornerRadius(12f)
                        .customView(R.layout.dialog_loading)
                        .lifecycleOwner(this)
                loadingDialog?.getCustomView()?.run {
                    this.findViewById<TextView>(R.id.loading_tips).text = message
//                    this.findViewById<ProgressBar>(R.id.progressBar).indeterminateTintList = SettingUtil.getOneColorStateList(it)
                }
            }
            loadingDialog?.show()
        }
    }
}

/**
 * 关闭等待框
 */
fun Activity.dismissLoadingExt() {
    loadingDialog?.dismiss()
    loadingDialog = null
}

/**
 * 关闭等待框
 */
fun Fragment.dismissLoadingExt() {
    loadingDialog?.dismiss()
    loadingDialog = null
}

fun Fragment.showTitleDialog(title: String, positiveText: String = "确定",
                             negativeText: String = "取消",
                             block: (MaterialDialog) -> Unit = {}) {
    activity?.let {
        MaterialDialog(it).cancelable(true)
                .title(text = title)
                .cancelOnTouchOutside(false)
                .cornerRadius(12f)
                .lifecycleOwner(this)
                .show {
                    positiveButton(text = positiveText) { dialog ->
                        block.invoke(dialog)
                    }
                    negativeButton(text = negativeText) {
                        it.dismiss()
                    }
                }
    }

}


fun AppCompatActivity.showCustomDialog(dialogBehavior: DialogBehavior = ModalDialog,
                                       layout: Int, block: (MaterialDialog) -> Unit): MaterialDialog {
    val dialog = MaterialDialog(this, dialogBehavior).show {
        customView(layout, noVerticalPadding = true)
        lifecycleOwner(this@showCustomDialog)

    }

    block.invoke(dialog)
    return dialog
}