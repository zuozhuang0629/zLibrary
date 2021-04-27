package com.wxzt.kfly.entitys

import com.wxzt.lib_common.enums.DetectionViewType

/**
 *  author      : zuoz
 *  date        : 2021/4/14 15:07
 *  description :
 */
data class TakeOffInfoEntity(
       var type: DetectionViewType,
       var info:String
) {
}