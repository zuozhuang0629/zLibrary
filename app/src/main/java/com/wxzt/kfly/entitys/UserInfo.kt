package com.wxzt.kfly.entitys

/**
 *  author : zuoz
 *  date : 2021/4/1 8:59
 *  description :
 */

data class UserInfo(
        var userName: String,
        val access_token: String,
        val expires_in: Int,
        val refresh_token: String,
        val scope: String,
        val token_type: String
) {
    fun getRequestToken(): String = "$token_type $access_token"

}
