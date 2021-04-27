package com.wxzt.lib_common.network.progress

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/11/14 10:47
 * Description:请求体进度回调接口，用于文件上传进度回
 * History:
 */
interface ProgressRequestListener {
    fun onRequestProgress(bytesWritten: Long, contentLength: Long, done: Boolean)
}