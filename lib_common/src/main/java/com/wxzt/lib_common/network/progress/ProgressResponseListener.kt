package com.wxzt.lib_common.network.progress

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/11/14 10:48
 * Description: 响应体进度回调接口，用于文件下载进度回调
 * History:
 */
interface ProgressResponseListener {
    fun onResponseProgress(bytesRead: Long, contentLength: Long, done: Boolean)
}