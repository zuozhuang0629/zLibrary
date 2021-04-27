package com.wxzt.kfly.ext

import org.angmarch.views.NiceSpinner
import org.angmarch.views.OnSpinnerItemSelectedListener
import java.util.*

/**
 *  author : zuoz
 *  date : 2021/4/2 17:06
 *  description :
 */
fun org.angmarch.views.NiceSpinner.initData(titles: List<String>, block: (Int, String) -> Unit) {
    this.attachDataSource(titles)
    this.setOnSpinnerItemSelectedListener { parent, view, position, id ->
        block.invoke(position, parent.getItemAtPosition(position).toString())
    }
}