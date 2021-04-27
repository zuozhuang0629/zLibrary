package com.wxzt.kfly.entitys

import dji.common.camera.SettingsDefinitions

/**
 *  author      : zuoz
 *  date        : 2021/4/26 14:21
 *  description :用于检测飞行时
 */
data class TaskOffCheckEntity(
        var isCoConnection: Boolean,
        var battery: Int,
        var satelliteCount: Int,
        val shootPhotoModed: SettingsDefinitions.ShootPhotoMode,
        val photoForMat: SettingsDefinitions.PhotoFileFormat,
        val sdStorage: Int

) {
}