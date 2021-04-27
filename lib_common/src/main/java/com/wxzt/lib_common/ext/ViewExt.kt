package com.wxzt.lib_common.ext

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 *  author : zuoz
 *  date : 2021/3/29 10:38
 *  description : view的常用扩展
 */


//私有扩展属性，允许2次点击的间隔时间
private var <T : View> T.delayTime: Long
    get() = getTag(0x7FFF0001) as? Long ?: 0
    set(value) {
        setTag(0x7FFF0001, value)
    }

//私有扩展属性，记录点击时的时间戳
private var <T : View> T.lastClickTime: Long
    get() = getTag(0x7FFF0002) as? Long ?: 0
    set(value) {
        setTag(0x7FFF0002, value)
    }

//私有扩展方法，判断能否触发点击事件
private fun <T : View> T.canClick(): Boolean {
    var flag = false
    var now = System.currentTimeMillis()
    if (now - this.lastClickTime >= this.delayTime) {
        flag = true
        this.lastClickTime = now
    }
    return flag
}

//扩展点击事件，默认 500ms 内不能触发 2 次点击
fun <T : View> T.clickWithDuration(time: Long = 500, block: (T) -> Unit) {
    delayTime = time
    setOnClickListener {
        if (canClick()) {
            block(this)
        }
    }
}

fun View.visibility(isShow: Boolean) {
    this.visibility = if (isShow) {
        View.VISIBLE
    } else {
        View.GONE
    }
}



/**
 * 自定义recycleview 初始化
 *
 * @param layoutManager 布局样式
 * @param bindAdapter   adapter
 * @param isScroll      是否滚动
 * @return
 */
fun RecyclerView.init(
        layoutManager: RecyclerView.LayoutManager, bindAdapter: RecyclerView.Adapter<*>,
        isScroll: Boolean = true
): RecyclerView {
    this.layoutManager = layoutManager
    this.adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}


