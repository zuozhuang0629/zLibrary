package com.wxzt.kfly.ustils

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.wxzt.kfly.entitys.UserInfo

/**
 *  author : zuoz
 *  date : 2021/4/1 17:11
 *  description :
 */
object CacheUtil {

    fun setUserInfo(user: UserInfo) {
        val data = GsonUtils.toJson(user)
        SPUtils.getInstance().put("user_key", data)
    }

    fun getUserInfo(): UserInfo? {
        val isExist = SPUtils.getInstance().contains("user_key")
        if (!isExist) {
            return null
        }

        val data = SPUtils.getInstance().getString("user_key")
        return GsonUtils.fromJson(data, UserInfo::class.java)

    }

    fun loginOut() {
        SPUtils.getInstance().put("user_key", "")
    }

    fun setIsLoginDji(isLogin: Boolean) {
        SPUtils.getInstance().put("is_login_dji", isLogin)
    }

    fun getIsLoginDji(): Boolean {
        if (!SPUtils.getInstance().contains("is_login_dji")) {
            return false
        }

        return SPUtils.getInstance().getBoolean("is_login_dji")
    }

    /**
     * 获取网络配置
     *
     * @return
     */
    fun getHttpConfig(): String? {
        if (!SPUtils.getInstance().contains("http_config")) {
            return null
        }
        return SPUtils.getInstance().getString("http_config")
    }

    /**
     * 设置网络配置
     *
     * @param http
     */
    fun setHttpConfig(http: String) {
        SPUtils.getInstance().put("http_config", http)
    }
}