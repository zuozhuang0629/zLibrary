package com.wxzt.kfly.dji

import android.content.Context
import android.os.AsyncTask
import com.blankj.utilcode.util.ToastUtils
import com.wxzt.kfly.appViewModel
import com.wxzt.kfly.enums.DjiProductStatus
import dji.common.error.DJIError
import dji.common.error.DJISDKError
import dji.sdk.base.BaseComponent
import dji.sdk.base.BaseProduct
import dji.sdk.sdkmanager.DJISDKInitEvent
import dji.sdk.sdkmanager.DJISDKManager
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/4
 * Description: DJI 注册帮助类
 */
object DJIRegisterHelper {
    private val isRegistrationInProgress = AtomicBoolean(false)
    private val mDJIComponentListener = BaseComponent.ComponentListener { isConnected: Boolean -> notifyStatusChange() }

    @JvmStatic
    fun startSDKRegistration(context: Context?) {
        if (isRegistrationInProgress.compareAndSet(false, true)) {
            AsyncTask.execute {
                DJISDKManager.getInstance().registerApp(context, object : DJISDKManager.SDKManagerCallback {
                    override fun onRegister(djiError: DJIError?) {

                        if (djiError === DJISDKError.REGISTRATION_SUCCESS) {
                            appViewModel.djiRegisterStatus.postValue(DjiProductStatus.REGISTRATION_SUCCESS)
                            DJISDKManager.getInstance().startConnectionToProduct()
                        } else {
                            ToastUtils.showLong("注册djisdk失败，请重新启动app")
                        }
                    }

                    override fun onProductDisconnect() {
                        appViewModel.djiRegisterStatus.postValue(DjiProductStatus.PRODUCT_DISCONNECT)

                    }

                    override fun onProductConnect(baseProduct: BaseProduct?) {
                        appViewModel.djiRegisterStatus.postValue(DjiProductStatus.PRODUCT_CONNECT)

                        baseProduct?.let {
//                            appViewModel.djiProduct.postValue(it.model)
                        }

                    }

                    override fun onProductChanged(p0: BaseProduct?) {

                    }

                    override fun onComponentChange(p0: BaseProduct.ComponentKey?, p1: BaseComponent?, p2: BaseComponent?) {
                        appViewModel.djiRegisterStatus.postValue(DjiProductStatus.PRODUCT_CHANGED)

                    }

                    override fun onInitProcess(p0: DJISDKInitEvent?, p1: Int) {
                    }

                    override fun onDatabaseDownloadProgress(p0: Long, p1: Long) {
                    }
                })
            }
        }
    }

    private fun notifyStatusChange() {}
}