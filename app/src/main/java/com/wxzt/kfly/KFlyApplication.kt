package com.wxzt.kfly

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.blankj.utilcode.util.LogUtils
import com.secneo.sdk.Helper
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.bugly.crashreport.CrashReport.UserStrategy
import com.wxzt.kfly.network.api.Api
import com.wxzt.kfly.ustils.DJIConnectionControlActivity
import com.wxzt.kfly.viewmodel.AppViewModel
import me.jessyan.retrofiturlmanager.RetrofitUrlManager

//Application全局的ViewModel，里面存放了一些账户信息，基本配置信息等
val appViewModel: AppViewModel by lazy { KFlyApplication.appViewModelInstance }

class KFlyApplication : Application(), ViewModelStoreOwner {
    private var mFactory: ViewModelProvider.Factory? = null
    private lateinit var mAppViewModelStore: ViewModelStore

    companion object {
        const val TAG = "kfly-----log"

        lateinit var app: Application
        lateinit var appViewModelInstance: AppViewModel
    }

    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
        app = this@KFlyApplication
        appViewModelInstance = getAppViewModelProvider().get(AppViewModel::class.java)
        val br: BroadcastReceiver = OnDJIUSBAttachedReceiver()
        val filter = IntentFilter()
        filter.addAction(DJIConnectionControlActivity.ACCESSORY_ATTACHED)
        registerReceiver(br, filter)
        initUtils()
    }

    override fun attachBaseContext(paramContext: Context) {
        super.attachBaseContext(paramContext)
        Helper.install(this@KFlyApplication)
    }

    private fun initUtils() {
        LogUtils.getConfig().globalTag = TAG
        RetrofitUrlManager.getInstance().setDebug(true)
        //将每个 BaseUrl 进行初始化,运行时可以随时改变 DOMAIN_NAME 对应的值,从而达到切换 BaseUrl 的效果
        RetrofitUrlManager.getInstance().putDomain(Api.BASE_TEST_NAME, Api.BASE_TEST_ADDRESS)
        RetrofitUrlManager.getInstance().putDomain(Api.BASE_PRO_NAME, Api.BASE_DEV_ADDRESS)
        RetrofitUrlManager.getInstance().putDomain(Api.BASE_DEV_NAME, Api.BASE_PRO_ADDRESS)

        //bugly
        val strategy = UserStrategy(this)
        strategy.appChannel = "zuoz"
        strategy.appVersion = "2.0"
        strategy.appPackageName = "com.wxzt.kfly"
        CrashReport.initCrashReport(this, "fccd8fd9de", true)

    }

    /**
     * 获取一个全局的ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }
}