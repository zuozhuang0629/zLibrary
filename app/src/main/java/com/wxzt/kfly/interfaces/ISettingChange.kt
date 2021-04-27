package com.wxzt.kfly.interfaces

import com.wxzt.kfly.db.entity.FlySettingEntity

/**
 *  author      : zuoz
 *  date        : 2021/4/8 10:33
 *  description : 设置参数变化监听
 */
interface ISettingChange {
    fun setDataChanged(flySettingEntity: FlySettingEntity?)
}