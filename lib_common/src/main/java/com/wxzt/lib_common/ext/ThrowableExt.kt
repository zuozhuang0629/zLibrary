package com.wxzt.lib_common.ext

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import com.wxzt.lib_common.network.HttpError
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 *  author : zuoz
 *  date : 2021/4/1 14:56
 *  description :
 */
fun Throwable.getHttpError(): HttpError {
    var ex: HttpError
    when (this) {
        is HttpException -> {
            ex = HttpError.NETWORK_ERROR
            return ex
        }
        is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
            ex = HttpError.PARSE_ERROR
            return ex
        }
        is ConnectException -> {
            ex = HttpError.NETWORK_ERROR
            return ex
        }
        is javax.net.ssl.SSLException -> {
            ex = HttpError.SSL_ERROR
            return ex
        }
        is ConnectTimeoutException -> {
            ex = HttpError.TIMEOUT_ERROR
            return ex
        }
        is java.net.SocketTimeoutException -> {
            ex = HttpError.TIMEOUT_ERROR
            return ex
        }
        is java.net.UnknownHostException -> {
            ex = HttpError.TIMEOUT_ERROR
            return ex
        }
        else -> {
            ex = HttpError.UNKNOWN
            return ex
        }
    }
}