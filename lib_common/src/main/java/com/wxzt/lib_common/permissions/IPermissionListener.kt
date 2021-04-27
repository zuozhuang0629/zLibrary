package com.wxzt.lib_common.permissions

import com.karumi.dexter.PermissionToken

/**
 *  author      : zuoz
 *  date        : 2021/4/26 17:17
 *  description :
 */
interface IPermissionListener {

    fun showPermissionGranted(permission: String)
    fun showPermissionDenied(permission: String, isPermanentlyDenied: Boolean)
    fun showPermissionRationale(token: PermissionToken)
}