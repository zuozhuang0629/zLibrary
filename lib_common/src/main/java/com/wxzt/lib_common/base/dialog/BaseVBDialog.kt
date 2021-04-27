package com.wyzt.lib_common.base.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

/**
 *  author      : zuoz
 *  date        : 2021/4/7 16:29
 *  description :
 */
abstract class BaseVBDialog<VB : ViewBinding> : DialogFragment() {
    protected lateinit var mViewBinding: VB
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE) //无标题
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setGravity(setDialogGravity())
            }
        }

        mViewBinding = getViewBinding()
        return mViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.apply {
            setCanceledOnTouchOutside(getIsOnTouchOutside())
            window?.apply {
                val wlp = this.attributes
                setDialogSize(wlp, this)
                wlp.windowAnimations = getAnim()
            }
        }

        initView()
    }


    abstract fun getLayoutId(): Int

    abstract fun setDialogGravity(): Int

    abstract fun getViewBinding(): VB

    abstract fun setDialogSize(wlp: WindowManager.LayoutParams, window: Window)

    abstract fun getAnim(): Int

    abstract fun getIsOnTouchOutside(): Boolean

    abstract fun initView()

}