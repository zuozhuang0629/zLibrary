package com.wxzt.kfly.entitys

import com.flyco.tablayout.listener.CustomTabEntity


/**
 * @Author:          zuoz
 * @CreateDate:     2021/2/2 9:58
 * @Description:
 */
data class TabEntity(
    val title: String,
    val icon: Int,
    val unIcon: Int
) : CustomTabEntity {
    override fun getTabTitle(): String = title

    override fun getTabSelectedIcon(): Int = icon

    override fun getTabUnselectedIcon(): Int = unIcon
}