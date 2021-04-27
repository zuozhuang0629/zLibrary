package com.wxzt.lib_common.ext.utils

import com.blankj.utilcode.util.GsonUtils
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.HashMap

/**
 *  author      : zuoz
 *  date        : 2021/4/20 11:30
 *  description :
 */

fun HashMap<String, String>.buildJSONRequestBody(): RequestBody =
        RequestBody.create(MediaType.parse("application/json"), GsonUtils.toJson(this))