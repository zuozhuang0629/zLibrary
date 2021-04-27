package com.wxzt.kfly.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.*
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.wxzt.kfly.base.BaseActivity
import com.wxzt.kfly.databinding.ActivitySplashBinding
import com.wxzt.kfly.dji.DJIRegisterHelper


class SplashActivity : BaseActivity<ActivitySplashBinding>() {


    override fun getViewBinding(): ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {

    }

    override fun initListener() {

    }

    override fun onResume() {
        super.onResume()
        missingPermission()
    }

    /**
     * 显示动画
     */
    private fun showAnimation() {
        val animation = ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, 1, 0.5f)
        animation.duration = 2000
        mViewBinding.ivIcon.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                gotoMainActivity()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    /**
     * 获取权限
     */
    private fun missingPermission() {

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.VIBRATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                )

                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            DJIRegisterHelper.startSDKRegistration(this@SplashActivity.applicationContext)
                            showAnimation()
                        } else {

                            ToastUtils.showLong("app需要读写权限")
                        }

                    }

                    override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        token?.continuePermissionRequest()
                    }

                })
                .withErrorListener {
                    val a = ""
                }
                .check()
//        rxPermissions.request(Manifest.permission.VIBRATE,
//                Manifest.permission.INTERNET,
//                Manifest.permission.ACCESS_WIFI_STATE,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_NETWORK_STATE,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.CHANGE_WIFI_STATE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//
//
//                Manifest.permission.RECORD_AUDIO)
//                .subscribe(Consumer { aBoolean: Boolean ->
//                    if (aBoolean) {
//                        DJIRegisterHelper.startSDKRegistration(this@SplashActivity.applicationContext)
//                        showAnimation()
//                    } else {
//                        Logger.i("权限未全部开启")
//                    }
//                }, Consumer { throwable: Throwable -> Logger.e("权限申请错误:%s", throwable.toString()) })
    }

    /**
     * 跳转MainActivity
     */
    private fun gotoMainActivity() {
        val intent: Intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}