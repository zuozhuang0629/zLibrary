package com.wxzt.lib_common.ext

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 *  author : zuoz
 *  date : 2021/3/29 10:40
 *  description : context 扩展
 */
//使用内联函数的泛型参数 reified 特性来实现
inline fun <reified T : Activity> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}





