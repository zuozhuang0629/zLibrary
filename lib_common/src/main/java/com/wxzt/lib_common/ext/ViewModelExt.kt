package com.wxzt.lib_common.ext

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wxzt.lib_common.base.activity.BaseVBActivity
import com.wxzt.lib_common.base.fragment.BaseVBFragment

import com.wxzt.lib_common.viewmodel.BaseViewModel
import com.wxzt.lib_common.network.HttpError
import com.wxzt.lib_common.network.state.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *  author : zuoz
 *  date : 2021/3/31 9:06
 *  description :
 */


/**
 * net request 不校验请求结果数据是否是成功
 * @param block 请求体方法
 */
fun <T> BaseViewModel.request(
        block: suspend () -> T,
        resultState: MutableLiveData<ResultState<T>>,
        isShowDialog: Boolean = false,
        loadingMessage: String = "请求网络中..."

): Job {
    return viewModelScope.launch {
        runCatching {
            if (isShowDialog) resultState.value = ResultState.onAppLoading(loadingMessage)
            //请求体
            block()
        }.onSuccess {
            resultState.value = ResultState.onAppSuccess(it)

        }.onFailure {
            resultState.value = ResultState.onAppError(it.getHttpError())
        }
    }
}


/**
 **
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVBActivity<*>.parseState(
        resultState: ResultState<T>,
        onSuccess: (T) -> Unit,
        onError: ((HttpError) -> Unit)? = null,
        onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.invoke()
        }
        is ResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.error) }
        }
    }
}

fun <T> BaseVBFragment<*>.parseState(
        resultState: ResultState<T>,
        onSuccess: (T) -> Unit,
        onError: ((HttpError) -> Unit)? = null,
        onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            onLoading?.invoke()
        }
        is ResultState.Success -> {
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            onError?.run { this(resultState.error) }
        }
    }
}

/**
 * 分装获取数据库数据
 *
 * @param T                 返回类型
 * @param block             查询方法
 * @param resultState       结果状态
 * @param isShowDialog      是否显示dialog
 * @param loadingMessage    加载信息
 * @receiver
 * @return
 */
fun <T> BaseViewModel.room(
        block: suspend () -> T,
        resultState: MutableLiveData<ResultState<T>>,
        isShowDialog: Boolean = false,
        loadingMessage: String = "正在加载中..."
): Job {
    return viewModelScope.launch {
        runCatching {
            if (isShowDialog) resultState.value = ResultState.onAppLoading(loadingMessage)
            //请求体
            block()
        }.onSuccess {
            resultState.value = ResultState.onAppSuccess(it)
        }.onFailure {
            resultState.value = ResultState.onAppError(it.getHttpError())
        }
    }
}

/**
 *  调用携程
 * @param block 操作耗时操作任务
 * @param success 成功回调
 * @param error 失败回调 可不给
 */
fun <T> BaseViewModel.launch(
        block: suspend() -> T,
        success: (T) -> Unit,
        error: (Throwable) -> Unit = {}
) {
    viewModelScope.launch {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                block()
            }
        }.onSuccess {
            success(it)
        }.onFailure {
            error(it)
        }
    }
}