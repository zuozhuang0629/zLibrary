package com.wxzt.lib_common.network.state

import com.wxzt.lib_common.network.HttpError


/**
 * 作者　: zuoz
 * 时间　: 2020/4/9
 * 描述　: 自定义结果集封装类
 */
sealed class ResultState<out T> {
    companion object {
        fun <T> onAppSuccess(data: T): ResultState<T> = Success(data)
        fun <T> onAppLoading(loadingMessage: String): ResultState<T> = Loading(loadingMessage)
        fun onAppError(error: HttpError): ResultState<Nothing> = Error(error)
    }

    data class Loading(val loadingMessage: String) : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val error: HttpError) : ResultState<Nothing>()
}
