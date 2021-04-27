package com.wxzt.kfly.network.api

/**
 *  author : zuoz
 *  date : 2021/3/31 14:20
 *  description :
 */
object Api {
    var BASE_TEST_ADDRESS = "http://k2u0a1f7u.kfgeo.com/"
    var BASE_DEV_ADDRESS = "http://192.168.1.117:8000/"
    var BASE_PRO_ADDRESS = "http://k2f0g1e7o.kfgeo.com"
    var LIVE_ADDRESS = "rtmp://k2u0a1f7u.kfgeo.com:18113/"
    var BASE_TEST_NAME = "TEST"
    var BASE_DEV_NAME = "DEV"
    var BASE_PRO_NAME = "PRO"

    fun getAddress(name: String): String = when (name) {
        BASE_TEST_NAME -> {
            BASE_TEST_ADDRESS
        }

        BASE_PRO_NAME -> {
            BASE_PRO_ADDRESS
        }

        BASE_DEV_NAME -> {
            BASE_DEV_ADDRESS
        }
        else -> {
            BASE_TEST_ADDRESS
        }
    }
}