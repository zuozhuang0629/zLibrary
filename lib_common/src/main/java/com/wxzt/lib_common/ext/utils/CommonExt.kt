package com.wyzt.lib_common.ext.utils

/**
 *  author : zuoz
 *  date : 2021/4/2 11:27
 *  description :
 */
/**
 * 判断是否为空 并传入相关操作
 */
inline fun <reified T> T?.notNull(notNullAction: (T) -> Unit, nullAction: () -> Unit = {}) {
    if (this != null) {
        notNullAction.invoke(this)
    } else {
        nullAction.invoke()
    }
}

@Suppress("NOTHING_TO_INLINE")
public inline fun <T : Enum<T>> T.toInt(): Int = this.ordinal

public inline fun <reified T : Enum<T>> Int.toEnum(): T = enumValues<T>()[this]