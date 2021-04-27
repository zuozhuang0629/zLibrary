package com.wxzt.kfly.enums

import androidx.constraintlayout.solver.widgets.analyzer.Direct

/**
 *  author : zuoz
 *  date : 2021/4/2 15:26
 *  description :任务枚举
 */
/**
 * 任务模式
 *
 * @constructor
 */
enum class TaskModelEnum(val vlaue: String) {
    POLYGON("01"),//多边形
    FIX("02"),//定点
    OBLIQUE("03"),//倾斜
    PANORAMIC("4");//全景


    fun getName(): String {
        return when (this) {
            POLYGON -> "多边形"
            FIX -> "定点"
            OBLIQUE -> "倾斜"
            PANORAMIC -> "全景"
        }
    }

    companion object {
        fun getModelEnum(name: String): TaskModelEnum {
            if (name == "多边形") {
                return TaskModelEnum.POLYGON
            } else if (name == "定点") {
                return TaskModelEnum.FIX
            } else if (name == "倾斜") {
                return TaskModelEnum.OBLIQUE
            } else if (name == "全景") {
                return TaskModelEnum.PANORAMIC
            } else {
                return TaskModelEnum.POLYGON
            }
        }
    }

}

/**
 * 任务进度枚举
 *
 * @constructor Create empty Task schedule enum
 */
enum class TaskStatusEnum(val vlaue: String) {
    NO_EXECUTE("00"),//未执行
    EXECUTING("01"),//进行中
    STOP("02"),//暂停
    COMPLETE("03"),//已完成
}

enum class TaskTypeEnum(val value: Int) {
    LOCATION(0),//本地任务
    SERVICE(1),//平台任务
    SERVICE_TO_LOCATION(2)//平台任务的本地任务

}

enum class LiveStatusEnum() {
    NO_LIVE,//没有直播
    LIVE_SUCCESS, //直播成功
    LIVE_CLOSE,//直播关闭
    LIVE_FAIL;//直播失败


    /**
     * 获取服务端 对应的string
     * 当在直播时 获取服务端的string 也是直播
     * 当你停止直播（除直播状态下）获取的是停止直播string
     *
     * @return
     */
    fun getStatusString(): String {
        return when (this) {
            LIVE_SUCCESS -> {
                "01"
            }
            else -> {
                "02"
            }
        }
    }
}