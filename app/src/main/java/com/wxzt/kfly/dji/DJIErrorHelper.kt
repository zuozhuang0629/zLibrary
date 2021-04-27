package com.wxzt.kfly.dji

import dji.common.error.DJIError
import dji.common.error.DJIFlightControllerError
import dji.common.error.DJIFlySafeError
import dji.common.error.DJIMissionError
import dji.common.mission.waypoint.WaypointMissionState

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/12 13:42
 * Description: dji错误信息帮助类
 * History:
 */
class DJIErrorHelper {
    fun showError(djiError: DJIError?) {}
    fun getCNInfoNormalDjiError(djiError: DJIError?): String {
        if (null == djiError) {
            return "正常执行"
        }
        return if (djiError == DJIFlightControllerError.CANNOT_TURN_OFF_MOTORS_WHILE_AIRCRAFT_FLYING) {
            "DJI:飞机正在飞行。出于安全考虑，电机无法关闭。"
        } else if (djiError == DJIFlightControllerError.COMMON_EXECUTION_FAILED) {
            "DJI:执行失败。"
        } else if (djiError == DJIFlightControllerError.GO_HOME_ALTITUDE_TOO_LOW) {
            "DJI:返程点海拔太低了（低于20米）。"
        } else if (djiError == DJIFlightControllerError.GO_HOME_ALTITUDE_TOO_HIGH) {
            "DJI:返程点海拔太高了（高于500米）。"
        } else if (djiError == DJIFlightControllerError.GO_HOME_ALTITUDE_HIGHER_THAN_MAX_FLIGHT_HEIGHT) {
            "DJI:返程点高度超过最大飞行高度。"
        } else if (djiError == DJIFlightControllerError.HOME_POINT_TOO_FAR) {
            "DJI:返程点距离太远。"
        } else if (djiError == DJIFlightControllerError.IMU_CALIBRATION_ERROR_IN_THE_AIR_OR_MOTORS_ON) {
            "DJI:飞机的电机已开启或飞机在空中，不允许进行IMU校准。"
        } else if (djiError == DJIFlightControllerError.INVALID_PARAMETER) {
            "DJI:参数无效。"
        } else if (djiError == DJIFlightControllerError.COULD_NOT_ENTER_TRANSPORT_MODE) {
            "DJI:飞机无法进入运输模式。"
        } else if (djiError == DJIFlightControllerError.GPS_SIGNAL_WEAK) {
            "DJI:GPS信号弱。"
        } else if (djiError == DJIFlightControllerError.ALREADY_IN_THE_AIR) {
            "DJI:飞机已经在空中。"
        } else if (djiError == DJIFlightControllerError.RTK_CANNOT_START) {
            "RTK定位无法正常启动请重启。"
        } else if (djiError == DJIFlightControllerError.RTK_CONNECTION_BROKEN) {
            "基站和移动设备之间的连接中断。"
        } else if (djiError == DJIFlightControllerError.RTK_BS_ANTENNA_ERROR) {
            "RTK基站天线错误检查天线是否连接到正确的端口。"
        } else if (djiError == DJIFlightControllerError.RTK_BS_COORDINATE_RESET) {
            "RTK基站的坐标重置。"
        } else if (djiError == DJIFlightControllerError.RTK_INTIALIZING) {
            "RTK定位初始化。"
        } else if (djiError == DJIFlightControllerError.RTK_NOT_ACTIVATED) {
            "RTK定位未激活。"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_NOT_FETCHED_LICENSE) {
            "DJI:白名单未获取许可证"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_ILLEGAL_INDEX) {
            "DJI:白名单非法指数"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_OPERATION_CODE_ERROR) {
            "DJI:白名单操作代码错误"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_REQ_ID_NOT_MATCHED) {
            "DJI:白名单ID不匹配"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_NEED_TO_CONNECT_NEWTORK) {
            "DJI:白名单需要连接纽约"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_LICENSE_NOT_SUPPORTED) {
            "DJI:不支持的白名单许可"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_LICENSE_IS_INVALID) {
            "DJI:白名单许可无效"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_FILE_SIZE_ERROR) {
            "DJI:白名单文件大小错误"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_INCORRECT_CRC) {
            "DJI:白名单校验错误"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_FAILED_TO_CHECK_SN) {
            "DJI:白名单未能检查SN"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_FLYC_VERSION_NOT_MATCHED) {
            "DJI:白名单FLYC版本不匹配"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_NO_FETCHED_LICENSE) {
            "DJI:白名单没有获得许可证"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_OPERATE_CODE_ERROR) {
            "DJI:白名单操作代码错误"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_REQ_ID_NOT_MATCH) {
            "DJI:白名单ID不匹配"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_LICENSE_NOT_SUPPORT) {
            "DJI:白名单许可证不支持"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_FAIL_TO_CHECK_SN) {
            "DJI:白名单无法检查SN"
        } else if (djiError == DJIFlightControllerError.WHITE_LIST_FLYC_VERSION_NOT_MATCH) {
            "DJI:白名单FLYC版本不匹配"
        } else {
            "DJI: " + djiError.description
        }
    }

    fun getCnInfoFromDjiWayPointMissionState(waypointMissionState: WaypointMissionState): String {
        return if (waypointMissionState === WaypointMissionState.NOT_SUPPORTED) {
            "连接的飞机不支持WayPointMission"
        } else if (waypointMissionState === WaypointMissionState.READY_TO_UPLOAD) {
            "已准备好上传任务"
        } else if (waypointMissionState === WaypointMissionState.UPLOADING) {
            "航点信息上传中"
        } else if (waypointMissionState === WaypointMissionState.READY_TO_EXECUTE) {
            "航点任务完全上传，飞机准备开始执行"
        } else if (waypointMissionState === WaypointMissionState.EXECUTING) {
            "执行成功启动"
        } else if (waypointMissionState === WaypointMissionState.EXECUTION_PAUSED) {
            "任务已成功暂停"
        } else if (waypointMissionState === WaypointMissionState.DISCONNECTED) {
            "移动设备，遥控器和飞机之间的连接断开"
        } else if (waypointMissionState === WaypointMissionState.RECOVERING) {
            "移动设备，遥控器和飞机之间的连接已建成，正在同步飞机的状态"
        } else if (waypointMissionState === WaypointMissionState.UNKNOWN) {
            "状态未知"
        } else {
            waypointMissionState.name
        }
    }

    fun getDjiError(djiError: String): String {
        if (djiError.contains(DJIFlySafeError.COULD_NOT_CONNECT_TO_INTERNET_FOR_PULLING_DATA.description)) {
            return djiError.replace(DJIFlySafeError.COULD_NOT_CONNECT_TO_INTERNET_FOR_PULLING_DATA.description, "DJI:SDK尝试从服务器提取最新的缓存数据时无法连接到网络。")
        }
        if (djiError.contains(DJIFlySafeError.COULD_NOT_FIND_UNLOCKED_RECORD_IN_THE_SERVER.description)) {
            return djiError.replace(DJIFlySafeError.COULD_NOT_FIND_UNLOCKED_RECORD_IN_THE_SERVER.description, "DJI:无法在服务器中找到未锁定的记录。")
        }
        if (djiError.contains(DJIFlySafeError.UNLOCKED_RECORD_NOT_FIND_ON_AIRCRAFT.description)) {
            return djiError.replace(DJIFlySafeError.UNLOCKED_RECORD_NOT_FIND_ON_AIRCRAFT.description, "DJI:无法在飞机上找到解锁记录。")
        }
        if (djiError.contains(DJIFlySafeError.NO_DATA_IN_DATABASE.description)) {
            return djiError.replace(DJIFlySafeError.UNLOCKED_RECORD_NOT_FIND_ON_AIRCRAFT.description, "DJI:数据库中没有数据。")
        }
        if (djiError.contains(DJIFlySafeError.NOT_LOGGED_IN.description)) {
            return djiError.replace(DJIFlySafeError.UNLOCKED_RECORD_NOT_FIND_ON_AIRCRAFT.description, "DJI:没有记录用户帐户。")
        }
        if (djiError.contains(DJIFlySafeError.ACCOUNT_NOT_LOGGED_IN_OR_NOT_AUTHORIZED.description)) {
            return djiError.replace(DJIFlySafeError.UNLOCKED_RECORD_NOT_FIND_ON_AIRCRAFT.description, "DJI:帐户未登录或帐户未获得授权。")
        }
        if (djiError.contains(DJIFlySafeError.FLIGHT_CONTROLLER_SERIAL_NUMBER_IS_NOT_READY.description)) {
            return djiError.replace(DJIFlySafeError.UNLOCKED_RECORD_NOT_FIND_ON_AIRCRAFT.description, "DJI:飞行控制器SN尚未就绪。")
        }
        if (djiError.contains(DJIFlySafeError.COULD_NOT_ENABLE_OR_DISABLE_GEO_SYSTEM_WHILE_AIRCRAFT_IS_IN_THE_SKY.description)) {
            return djiError.replace(DJIFlySafeError.UNLOCKED_RECORD_NOT_FIND_ON_AIRCRAFT.description, "DJI:在飞机飞行时无法启用或禁用GEO系统。")
        }
        if (djiError.contains(DJIFlySafeError.INVALID_SIMULATED_LOCATION.description)) {
            return djiError.replace(DJIFlySafeError.UNLOCKED_RECORD_NOT_FIND_ON_AIRCRAFT.description, "DJI:模拟的飞机位置无效。")
        }
        if (djiError.contains(DJIFlySafeError.USER_MISMATCH.description)) {
            return djiError.replace(DJIFlySafeError.UNLOCKED_RECORD_NOT_FIND_ON_AIRCRAFT.description, "DJI:用户不匹配。")
        }
        if (djiError.contains("Camera is busy or the command is not supported in the Camera's current state")) {
            return djiError.replace("Camera is busy or the command is not supported in the Camera's current state", "相机忙，相机当前状态不支持命令")
        }
        if (djiError.contains(DJIMissionError.MISSION_RADIUS_OVER_LIMIT.description)) {
            return djiError.replace(DJIMissionError.MISSION_RADIUS_OVER_LIMIT.description, "任务点超过飞行半径限制")
        }
        if (djiError.contains(DJIMissionError.MAX_FLIGHT_SPEED_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.MAX_FLIGHT_SPEED_NOT_VALID.description, "提供的最大飞行速度值是无效的")
        }
        if (djiError.contains(DJIMissionError.AUTO_FLIGHT_SPEED_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.AUTO_FLIGHT_SPEED_NOT_VALID.description, "提供的自动飞行速度值是无效的")
        }
        if (djiError.contains(DJIMissionError.REPEAT_TIME_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.REPEAT_TIME_NOT_VALID.description, "重复提供的时间值是无效的")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_COUNT_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_COUNT_NOT_VALID.description, "任务点数是无效的")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_LIST_SIZE_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_LIST_SIZE_NOT_VALID.description, "任务点列表大小是无效的")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_COORDINATE_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_COORDINATE_NOT_VALID.description, "提供任务点坐标无效")
        }
        if (djiError.contains(DJIMissionError.ALTITUDE_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.ALTITUDE_NOT_VALID.description, "提供任务点高度无效")
        }
        if (djiError.contains(DJIMissionError.HEADING_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.HEADING_NOT_VALID.description, "提供的航路点是无效的")
        }
        if (djiError.contains(DJIMissionError.ACTION_REPEAT_TIME_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.ACTION_REPEAT_TIME_NOT_VALID.description, "提供的路标重复时间是无效的")
        }
        if (djiError.contains(DJIMissionError.ACTION_TIMEOUT_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.ACTION_TIMEOUT_NOT_VALID.description, "提供的航路点操作超时是无效的")
        }
        if (djiError.contains(DJIMissionError.CORNER_RADIUS_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.CORNER_RADIUS_NOT_VALID.description, "提供的航路点半径是无效的")
        }
        if (djiError.contains(DJIMissionError.GIMBAL_PITCH_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.GIMBAL_PITCH_NOT_VALID.description, "提供航线点平衡程度是无效的")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_SPEED_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_SPEED_NOT_VALID.description, "提供的航路点速度是无效的")
        }
        if (djiError.contains(DJIMissionError.SHOOT_PHOTO_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.SHOOT_PHOTO_NOT_VALID.description, "提供的航路点拍摄距离是无效的")
        }
        if (djiError.contains(DJIMissionError.STAY_ACTION_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.STAY_ACTION_NOT_VALID.description, "航路点行为参数是无效的")
        }
        if (djiError.contains(DJIMissionError.ROTATE_GIMBAL_ACTION_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.ROTATE_GIMBAL_ACTION_NOT_VALID.description, "提供的航线点旋转万向节操作参数是无效的")
        }
        if (djiError.contains(DJIMissionError.ROTATE_AIRCRAFT_ACTION_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.ROTATE_AIRCRAFT_ACTION_NOT_VALID.description, "航路点旋转飞机行动帕拉姆是无效的")
        }
        if (djiError.contains(DJIMissionError.NULL_MISSION.description)) {
            return djiError.replace(DJIMissionError.NULL_MISSION.description, "空任务")
        }
        if (djiError.contains(DJIMissionError.INCOMPLETE_MISSION.description)) {
            return djiError.replace(DJIMissionError.INCOMPLETE_MISSION.description, "不完整任务")
        }
        if (djiError.contains(DJIMissionError.COMMON_UNSUPPORTED.description)) {
            return djiError.replace(DJIMissionError.COMMON_UNSUPPORTED.description, "当前任务不支持") //很多地方都有not support 这里是任务错误应该是任务不支持
        }
        if (djiError.contains(DJIMissionError.COMMON_DISCONNECTED.description)) {
            return djiError.replace(DJIMissionError.COMMON_DISCONNECTED.description, "任务断开了")
        }
        if (djiError.contains(DJIMissionError.BEGAN.description)) {
            return djiError.replace(DJIMissionError.BEGAN.description, "该任务已经开始了")
        }
        if (djiError.contains(DJIMissionError.CANCELED.description)) {
            return djiError.replace(DJIMissionError.CANCELED.description, "该任务已经取消了")
        }
        if (djiError.contains(DJIMissionError.FAILED.description)) {
            return djiError.replace(DJIMissionError.FAILED.description, "该任务执行失败")
        }
        if (djiError.contains(DJIMissionError.NO_MISSION_RUNNING.description)) {
            return djiError.replace(DJIMissionError.NO_MISSION_RUNNING.description, "该任务不存在")
        }
        if (djiError.contains(DJIMissionError.TIMEOUT.description)) {
            return djiError.replace(DJIMissionError.TIMEOUT.description, "该任务执行超时了")
        }
        if (djiError.contains(DJIMissionError.MODE_ERROR.description)) {
            return djiError.replace(DJIMissionError.MODE_ERROR.description, "飞机的控制模式不是正确的")
        }
        if (djiError.contains(DJIMissionError.GPS_NOT_READY.description)) {
            return djiError.replace(DJIMissionError.GPS_NOT_READY.description, "飞机的GPS还没有准备好")
        }
        if (djiError.contains(DJIMissionError.MOTORS_DID_NOT_START.description)) {
            return djiError.replace(DJIMissionError.MOTORS_DID_NOT_START.description, "飞机的发动机还没有启动")
        }
        if (djiError.contains(DJIMissionError.TAKE_OFF.description)) {
            return djiError.replace(DJIMissionError.TAKE_OFF.description, "飞机正在起飞")
        }
        if (djiError.contains(DJIMissionError.IS_FLYING.description)) {
            return djiError.replace(DJIMissionError.IS_FLYING.description, "飞机已经是飞行的")
        }
        if (djiError.contains(DJIMissionError.NOT_AUTO_MODE.description)) {
            return djiError.replace(DJIMissionError.NOT_AUTO_MODE.description, "飞机不在自动模式下")
        }
        if (djiError.contains(DJIMissionError.MAX_NUMBER_OF_WAYPOINTS_UPLOAD_LIMIT_REACHED.description)) {
            return djiError.replace(DJIMissionError.MAX_NUMBER_OF_WAYPOINTS_UPLOAD_LIMIT_REACHED.description, "航路点的任务达到了最大值点 ")
        }
        if (djiError.contains(DJIMissionError.UPLOADING_WAYPOINT.description)) {
            return djiError.replace(DJIMissionError.UPLOADING_WAYPOINT.description, "任务航线点正在上传")
        }
        if (djiError.contains(DJIMissionError.KEY_LEVEL_LOW.description)) {
            return djiError.replace(DJIMissionError.KEY_LEVEL_LOW.description, "提供给您的API密钥并不是在正确的权限级别上")
        }
        if (djiError.contains(DJIMissionError.NAVIGATION_MODE_DISABLED.description)) {
            return djiError.replace(DJIMissionError.NAVIGATION_MODE_DISABLED.description, "导航是不开放的")
        }
        if (djiError.contains(DJIMissionError.TOO_CLOSE_TO_HOME_POINT.description)) {
            return djiError.replace(DJIMissionError.TOO_CLOSE_TO_HOME_POINT.description, "飞机离返航点太近了")
        }
        if (djiError.contains(DJIMissionError.IOC_TYPE_UNKNOWN.description)) {
            return djiError.replace(DJIMissionError.IOC_TYPE_UNKNOWN.description, "IOC对这个类型未知")
        }
        if (djiError.contains(DJIMissionError.HOME_POINT_VALUE_INVALID.description)) {
            return djiError.replace(DJIMissionError.HOME_POINT_VALUE_INVALID.description, "这个返航点不是有效的浮点型值")
        }
        if (djiError.contains(DJIMissionError.HOME_POINT_LOCATION_INVALID.description)) {
            return djiError.replace(DJIMissionError.HOME_POINT_LOCATION_INVALID.description, "返航点的纬度和经度是无效的")
        }
        if (djiError.contains(DJIMissionError.HOME_POINT_DIRECTION_UNKNOWN.description)) {
            return djiError.replace(DJIMissionError.HOME_POINT_DIRECTION_UNKNOWN.description, "返航点的方向是未知的")
        }
        if (djiError.contains(DJIMissionError.HOME_POINT_MISSION_PAUSED.description)) {
            return djiError.replace(DJIMissionError.HOME_POINT_MISSION_PAUSED.description, "返航任务是暂停的")
        }
        if (djiError.contains(DJIMissionError.HOME_POINT_MISSION_NOT_PAUSED.description)) {
            return djiError.replace(DJIMissionError.HOME_POINT_MISSION_NOT_PAUSED.description, "飞机和手机之间的距离是不可接受的极限（必须低于20000米）")
        }
        if (djiError.contains(DJIMissionError.FOLLOW_ME_DISTANCE_TOO_LARGE.description)) {
            return djiError.replace(DJIMissionError.FOLLOW_ME_DISTANCE_TOO_LARGE.description, "跟随模式任务太长")
        }
        if (djiError.contains(DJIMissionError.FOLLOW_ME_DISCONNECT_TIME_TOO_LONG.description)) {
            return djiError.replace(DJIMissionError.FOLLOW_ME_DISCONNECT_TIME_TOO_LONG.description, "跟随我任务的分离时间太长了")
        }
        if (djiError.contains(DJIMissionError.FOLLOW_ME_GIMBAL_PITCH_ERROR.description)) {
            return djiError.replace(DJIMissionError.FOLLOW_ME_GIMBAL_PITCH_ERROR.description, "万向架的初始俯仰角太大了")
        }
        if (djiError.contains(DJIMissionError.ALTITUDE_TOO_HIGH.description)) {
            return djiError.replace(DJIMissionError.ALTITUDE_TOO_HIGH.description, "飞行高度高于限制飞行高度")
        }
        if (djiError.contains(DJIMissionError.ALTITUDE_TOO_LOW.description)) {
            return djiError.replace(DJIMissionError.ALTITUDE_TOO_LOW.description, "飞行高度低于限制飞行高度")
        }
        if (djiError.contains(DJIMissionError.MISSION_RADIUS_INVALID.description)) {
            return djiError.replace(DJIMissionError.MISSION_RADIUS_INVALID.description, "任务的半径是无效的")
        }
        if (djiError.contains(DJIMissionError.MISSION_SPEED_TOO_HIGH.description)) {
            return djiError.replace(DJIMissionError.MISSION_SPEED_TOO_HIGH.description, "任务的速度太大了")
        }
        if (djiError.contains(DJIMissionError.MISSION_ENTRY_POINT_INVALID.description)) {
            return djiError.replace(DJIMissionError.MISSION_ENTRY_POINT_INVALID.description, "任务的入口点是无效的")
        }
        if (djiError.contains(DJIMissionError.MISSION_HEADING_MODE_INVALID.description)) {
            return djiError.replace(DJIMissionError.MISSION_HEADING_MODE_INVALID.description, "任务的标题模式是无效的")
        }
        if (djiError.contains(DJIMissionError.MISSION_RESUME_FAILED.description)) {
            return djiError.replace(DJIMissionError.MISSION_RESUME_FAILED.description, "未能继续执行任务")
        }
        if (djiError.contains(DJIMissionError.NAVIGATION_MODE_NOT_SUPPORTED.description)) {
            return djiError.replace(DJIMissionError.NAVIGATION_MODE_NOT_SUPPORTED.description, "不支持导航模式")
        }
        if (djiError.contains(DJIMissionError.DISTANCE_FROM_MISSION_TARGET_TOO_LONG.description)) {
            return djiError.replace(DJIMissionError.DISTANCE_FROM_MISSION_TARGET_TOO_LONG.description, "产品中的导航不受支持")
        }
        if (djiError.contains(DJIMissionError.RC_MODE_ERROR.description)) {
            return djiError.replace(DJIMissionError.RC_MODE_ERROR.description, "模式错误，请确保远程控制器的模式开关在里面")
        }
        if (djiError.contains(DJIMissionError.IOC_WORKING.description)) {
            return djiError.replace(DJIMissionError.IOC_WORKING.description, "IOC模式正在发挥作用")
        }
        if (djiError.contains(DJIMissionError.MISSION_NOT_INITIALIZED.description)) {
            return djiError.replace(DJIMissionError.MISSION_NOT_INITIALIZED.description, "任务没有初始化")
        }
        if (djiError.contains(DJIMissionError.MISSION_NOT_EXIST.description)) {
            return djiError.replace(DJIMissionError.MISSION_NOT_EXIST.description, "这个任务不存在")
        }
        if (djiError.contains(DJIMissionError.MISSION_CONFLICT.description)) {
            return djiError.replace(DJIMissionError.MISSION_CONFLICT.description, "在任务中有一个相互冲突的环境")
        }
        if (djiError.contains(DJIMissionError.MISSION_ESTIMATE_TIME_TOO_LONG.description)) {
            return djiError.replace(DJIMissionError.MISSION_ESTIMATE_TIME_TOO_LONG.description, "这次任务的估计时间太长了")
        }
        if (djiError.contains(DJIMissionError.IN_NOVICE_MODE.description)) {
            return djiError.replace(DJIMissionError.IN_NOVICE_MODE.description, "飞机现在处于初级模式")
        }
        if (djiError.contains(DJIMissionError.HIGH_PRIORITY_MISSION_EXECUTING.description)) {
            return djiError.replace(DJIMissionError.HIGH_PRIORITY_MISSION_EXECUTING.description, "一个更高优先级的任务正在执行")
        }
        if (djiError.contains(DJIMissionError.GPS_SIGNAL_WEAK.description)) {
            return djiError.replace(DJIMissionError.GPS_SIGNAL_WEAK.description, "这架飞机的GPS信号很弱")
        }
        if (djiError.contains(DJIMissionError.LOW_BATTERY.description)) {
            return djiError.replace(DJIMissionError.LOW_BATTERY.description, "电池电量过低液位报警")
        }
        if (djiError.contains(DJIMissionError.AIRCRAFT_NOT_IN_THE_AIR.description)) {
            return djiError.replace(DJIMissionError.AIRCRAFT_NOT_IN_THE_AIR.description, "飞机不在空中")
        }
        if (djiError.contains(DJIMissionError.MISSION_PARAMETERS_INVALID.description)) {
            return djiError.replace(DJIMissionError.MISSION_PARAMETERS_INVALID.description, "任务的参数是无效的")
        }
        if (djiError.contains(DJIMissionError.MISSION_CONDITION_NOT_SATISFIED.description)) {
            return djiError.replace(DJIMissionError.MISSION_CONDITION_NOT_SATISFIED.description, "任务的条件不满足")
        }
        if (djiError.contains(DJIMissionError.MISSION_ACROSS_NO_FLY_ZONE.description)) {
            return djiError.replace(DJIMissionError.MISSION_ACROSS_NO_FLY_ZONE.description, "任务就在禁飞区")
        }
        if (djiError.contains(DJIMissionError.HOME_POINT_NOT_RECORDED.description)) {
            return djiError.replace(DJIMissionError.HOME_POINT_NOT_RECORDED.description, "飞机的主点没有被记录下来")
        }
        if (djiError.contains(DJIMissionError.AIRCRAFT_IN_NO_FLY_ZONE.description)) {
            return djiError.replace(DJIMissionError.AIRCRAFT_IN_NO_FLY_ZONE.description, "飞机在禁飞区")
        }
        if (djiError.contains(DJIMissionError.MISSION_INFO_INVALID.description)) {
            return djiError.replace(DJIMissionError.MISSION_INFO_INVALID.description, "任务的信息是无效的")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_INFO_INVALID.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_INFO_INVALID.description, "任务点的信息是无效的")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_TRACE_TOO_LONG.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_TRACE_TOO_LONG.description, "任务点轨迹太长了")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_TOTAL_TRACE_TOO_LONG.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_TOTAL_TRACE_TOO_LONG.description, "航路点的总轨迹太长了")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_INDEX_OVER_RANGE.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_INDEX_OVER_RANGE.description, "任务点的索引超过了范围")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_DISTANCE_TOO_CLOSE.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_DISTANCE_TOO_CLOSE.description, "距离太近了")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_DISTANCE_TOO_LONG.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_DISTANCE_TOO_LONG.description, "任务点的距离太长了")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_DAMPING_CHECK_FAILED.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_DAMPING_CHECK_FAILED.description, "damping检测失败")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_ACTION_PARAMETER_INVALID.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_ACTION_PARAMETER_INVALID.description, "任务点操作的参数是无效的")
        }
        if (djiError.contains(DJIMissionError.INDICES_ARE_DISCONTINUOUS.description)) {
            return djiError.replace(DJIMissionError.INDICES_ARE_DISCONTINUOUS.description, "索引是不连续的")
        }
        if (djiError.contains(DJIMissionError.ACTION_REPEAT_TIME_NOT_VALID.description)) {
            return djiError.replace(DJIMissionError.ACTION_REPEAT_TIME_NOT_VALID.description, "动作重复时间无效")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_MISSION_INFO_NOT_UPLOADED.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_MISSION_INFO_NOT_UPLOADED.description, "任务点任务的信息并没有完全上传")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_UPLOAD_NOT_COMPLETE.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_UPLOAD_NOT_COMPLETE.description, "上传的方式还不完整")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_REQUEST_IS_RUNNING.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_REQUEST_IS_RUNNING.description, "这个任务点请求正在执行")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_NOT_RUNNING.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_NOT_RUNNING.description, "这个任务点任务没有被执行")
        }
        if (djiError.contains(DJIMissionError.WAYPOINT_IDLE_VELOCITY_INVALID.description)) {
            return djiError.replace(DJIMissionError.WAYPOINT_IDLE_VELOCITY_INVALID.description, "0速度是无效的")
        }
        if (djiError.contains(DJIMissionError.AIRCRAFT_TAKING_OFF.description)) {
            return djiError.replace(DJIMissionError.AIRCRAFT_TAKING_OFF.description, "飞机起飞了")
        }
        if (djiError.contains(DJIMissionError.AIRCRAFT_LANDING.description)) {
            return djiError.replace(DJIMissionError.AIRCRAFT_LANDING.description, "飞机正在着陆")
        }
        if (djiError.contains(DJIMissionError.AIRCRAFT_GOING_HOME.description)) {
            return djiError.replace(DJIMissionError.AIRCRAFT_GOING_HOME.description, "飞机正在返航")
        }
        if (djiError.contains(DJIMissionError.AIRCRAFT_STARTING_MOTOR.description)) {
            return djiError.replace(DJIMissionError.AIRCRAFT_STARTING_MOTOR.description, "飞机正在启动马达")
        }
        if (djiError.contains(DJIMissionError.WRONG_CMD.description)) {
            return djiError.replace(DJIMissionError.WRONG_CMD.description, "错误命令")
        }
        if (djiError.contains(DJIMissionError.RUNNING_MISSION.description)) {
            return djiError.replace(DJIMissionError.RUNNING_MISSION.description, "这架飞机正在执行任务")
        }
        if (djiError.contains(DJIMissionError.TRACKING_TARGET_LOW_CONFIDENCE.description)) {
            return djiError.replace(DJIMissionError.TRACKING_TARGET_LOW_CONFIDENCE.description, "任务太不确定跟踪对象")
        }
        if (djiError.contains(DJIMissionError.TRACKING_PAUSED_BY_USER.description)) {
            return djiError.replace(DJIMissionError.TRACKING_PAUSED_BY_USER.description, "任务被用户暂停")
        }
        if (djiError.contains(DJIMissionError.TRACKING_TARGET_TOO_HIGH.description)) {
            return djiError.replace(DJIMissionError.TRACKING_TARGET_TOO_HIGH.description, "跟踪目标太高了")
        }
        if (djiError.contains(DJIMissionError.TRACKING_OBSTACLE_DETECTED.description)) {
            return djiError.replace(DJIMissionError.TRACKING_OBSTACLE_DETECTED.description, "检测到障碍物")
        }
        if (djiError.contains(DJIMissionError.TRACKING_GIMBAL_PITCH_TOO_LOW.description)) {
            return djiError.replace(DJIMissionError.TRACKING_GIMBAL_PITCH_TOO_LOW.description, "万向节的音高太低了")
        }
        if (djiError.contains(DJIMissionError.TRACKING_TARGET_TOO_FAR.description)) {
            return djiError.replace(DJIMissionError.TRACKING_TARGET_TOO_FAR.description, "跟踪目标离飞机太远了")
        }
        if (djiError.contains(DJIMissionError.TRACKING_TARGET_TOO_CLOSE.description)) {
            return djiError.replace(DJIMissionError.TRACKING_TARGET_TOO_CLOSE.description, "跟踪目标离飞机太近了")
        }
        if (djiError.contains(DJIMissionError.AIRCRAFT_ALTITUDE_TOO_HIGH.description)) {
            return djiError.replace(DJIMissionError.AIRCRAFT_ALTITUDE_TOO_HIGH.description, "飞机的高度太高了")
        }
        if (djiError.contains(DJIMissionError.AIRCRAFT_ALTITUDE_TOO_LOW.description)) {
            return djiError.replace(DJIMissionError.AIRCRAFT_ALTITUDE_TOO_LOW.description, "飞机的高度太低了")
        }
        if (djiError.contains(DJIMissionError.TRACKING_RECT_TOO_SMALL.description)) {
            return djiError.replace(DJIMissionError.TRACKING_RECT_TOO_SMALL.description, "跟踪矩形太小了")
        }
        if (djiError.contains(DJIMissionError.TRACKING_RECT_TOO_LARGE.description)) {
            return djiError.replace(DJIMissionError.TRACKING_RECT_TOO_LARGE.description, "跟踪矩形太大了")
        }
        if (djiError.contains(DJIMissionError.TRACKING_TARGET_NOT_ENOUGH_FEATURES.description)) {
            return djiError.replace(DJIMissionError.TRACKING_TARGET_NOT_ENOUGH_FEATURES.description, "跟踪目标没有足够的特性来锁定")
        }
        if (djiError.contains(DJIMissionError.TRACKING_TARGET_LOST.description)) {
            return djiError.replace(DJIMissionError.TRACKING_TARGET_LOST.description, "跟踪目标丢失了")
        }
        if (djiError.contains(DJIMissionError.VISION_DATA_ABNORMAL.description)) {
            return djiError.replace(DJIMissionError.VISION_DATA_ABNORMAL.description, "来自视觉系统的数据是不正常的")
        }
        if (djiError.contains(DJIMissionError.NO_VIDEO_FEED.description)) {
            return djiError.replace(DJIMissionError.NO_VIDEO_FEED.description, "没有为任务捕获实时视频提要")
        }
        if (djiError.contains(DJIMissionError.VIDEO_FRAME_RATE_TOO_LOW.description)) {
            return djiError.replace(DJIMissionError.VIDEO_FRAME_RATE_TOO_LOW.description, "视频直播的帧率太低了")
        }
        if (djiError.contains(DJIMissionError.REACH_FLIGHT_LIMITATION.description)) {
            return djiError.replace(DJIMissionError.REACH_FLIGHT_LIMITATION.description, "飞机已经达到飞行限制")
        }
        if (djiError.contains(DJIMissionError.VISION_SYSTEM_NOT_AUTHORIZED.description)) {
            return djiError.replace(DJIMissionError.VISION_SYSTEM_NOT_AUTHORIZED.description, "视觉系统无法获得控制飞机的授权")
        }
        if (djiError.contains(DJIMissionError.TRACKING_TARGET_SHAKING.description)) {
            return djiError.replace(DJIMissionError.TRACKING_TARGET_SHAKING.description, "跟踪目标抖动得太厉害了")
        }
        if (djiError.contains(DJIMissionError.POINTING_AIRCRAFT_NOT_IN_THE_AIR.description)) {
            return djiError.replace(DJIMissionError.POINTING_AIRCRAFT_NOT_IN_THE_AIR.description, "飞机不在空中。请先起飞")
        }
        if (djiError.contains(DJIMissionError.VISION_SYSTEM_NEEDS_CALIBRATION.description)) {
            return djiError.replace(DJIMissionError.VISION_SYSTEM_NEEDS_CALIBRATION.description, "视觉系统需要校准")
        }
        if (djiError.contains(DJIMissionError.FEATURE_POINT_CANNOT_MATCH.description)) {
            return djiError.replace(DJIMissionError.FEATURE_POINT_CANNOT_MATCH.description, "两个视觉传感器发现的特征点是不匹配的")
        }
        if (djiError.contains(DJIMissionError.VISION_SENSOR_OVEREXPOSED.description)) {
            return djiError.replace(DJIMissionError.VISION_SENSOR_OVEREXPOSED.description, "视觉传感器被过度曝光")
        }
        if (djiError.contains(DJIMissionError.VISION_SENSOR_UNDEREXPOSED.description)) {
            return djiError.replace(DJIMissionError.VISION_SENSOR_UNDEREXPOSED.description, "视觉传感器被曝光不足")
        }
        if (djiError.contains(DJIMissionError.VISION_SENSOR_LOW_QUALITY.description)) {
            return djiError.replace(DJIMissionError.VISION_SENSOR_LOW_QUALITY.description, "视觉传感器的质量很低")
        }
        if (djiError.contains(DJIMissionError.VISION_SYSTEM_ERROR.description)) {
            return djiError.replace(DJIMissionError.VISION_SYSTEM_ERROR.description, "视觉系统遇到系统错误")
        }
        if (djiError.contains(DJIMissionError.REACH_ALTITUDE_LOWER_BOUND.description)) {
            return djiError.replace(DJIMissionError.REACH_ALTITUDE_LOWER_BOUND.description, "飞机到达了飞蝇任务的高度")
        }
        if (djiError.contains(DJIMissionError.CANNOT_BYPASS_OBSTACLE.description)) {
            return djiError.replace(DJIMissionError.CANNOT_BYPASS_OBSTACLE.description, "飞机无法绕过障碍物")
        }
        if (djiError.contains(DJIMissionError.STOPPED_BY_USER.description)) {
            return djiError.replace(DJIMissionError.STOPPED_BY_USER.description, "任务被用户阻止了")
        }
        if (djiError.contains(DJIMissionError.MISSION_NOT_STARTED.description)) {
            return djiError.replace(DJIMissionError.MISSION_NOT_STARTED.description, "任务还没有开始")
        }
        if (djiError.contains(DJIMissionError.UNKNOWN.description)) {
            return djiError.replace(DJIMissionError.UNKNOWN.description, "未知的结果")
        }
        if (djiError.contains(DJIMissionError.Z30_ONLY_SUPPORT_SPOTLIGHT_PRO.description)) {
            return djiError.replace(DJIMissionError.Z30_ONLY_SUPPORT_SPOTLIGHT_PRO.description, "目前的相机只支持spotlight pro模式")
        }


        //ActiveTrackState
        if (djiError.contains("Not Support")) {
            return djiError.replace("Not Support", "不支持")
        }
        if (djiError.contains("UNKNOWN")) {
            return djiError.replace("UNKNOWN", "操作人员的状态是未知的。它是初始状态当操作符刚刚创建时")
        }
        if (djiError.contains("DISCONNECTED")) {
            return djiError.replace("DISCONNECTED", "移动设备和飞机之间的联系被打破了")
        }
        if (djiError.contains("CANNOT START")) {
            return djiError.replace("CANNOT START", "现有的错误阻止了任何任务的启动")
        }
        if (djiError.contains("AUTO SENSING")) {
            return djiError.replace("AUTO SENSING", "主动跟踪传感器被启用，飞机自动检测目标。如果找到了目标，用户可以选择正确的跟踪对象。在这个状态下，用户仍然可以手动绘制矩形")
        }
        if (djiError.contains("AUTO SENSING FOR QUICK SHOT")) {
            return djiError.replace("AUTO SENSING FOR QUICK SHOT", "快速射击传感器被启用")
        }
        if (djiError.contains("DETECTING HUMAN")) {
            return djiError.replace("DETECTING HUMAN", "手势模式被启用")
        }
        if (djiError.contains("WAITING FOR CONFIRMATION")) {
            return djiError.replace("WAITING FOR CONFIRMATION", "相机已经在追踪目标了。为了使飞机跟踪目标，需要用户的确认")
        }
        if (djiError.contains("CANNOT CONFIRM")) {
            return djiError.replace("CANNOT CONFIRM", "相机已经在跟踪目标，但有一个错误阻止了飞机跟随目标")
        }
        if (djiError.contains("AIRCRAFT FOLLOWING")) {
            return djiError.replace("AIRCRAFT FOLLOWING", "目标是发现。摄像机已经在跟踪目标，飞机也在跟踪目标")
        }
        if (djiError.contains("ONLY CAMERA FOLLOWING")) {
            return djiError.replace("ONLY CAMERA FOLLOWING", "在Splotlight模式或Spotlight Pro模式下执行一项ActiveTrack任务")
        }
        if (djiError.contains("PERFORMING QUICK SHOT")) {
            return djiError.replace("PERFORMING QUICK SHOT", "操作员正在执行一个快速射击任务")
        }
        if (djiError.contains("FINDING TRACKED TARGET")) {
            return djiError.replace("FINDING TRACKED TARGET", "ActiveTrack启动了，但是目标丢失了")
        }
        if (djiError.contains("IDLE")) {
            return djiError.replace("IDLE", "任务是空的")
        }
        if (djiError.contains(DJIError.CANNOT_PAUSE_STABILIZATION.description)) {
            return djiError.replace(DJIError.CANNOT_PAUSE_STABILIZATION.description, "不能暂停稳定")
        }
        if (djiError.contains(DJIError.COMMON_EXECUTION_FAILED.description)) {
            return djiError.replace(DJIError.COMMON_EXECUTION_FAILED.description, "任务不能被执行")
        }
        if (djiError.contains(DJIError.COMMON_SYSTEM_BUSY.description)) {
            return djiError.replace(DJIError.COMMON_SYSTEM_BUSY.description, "系统太忙而无法执行操作")
        }
        if (djiError.contains(DJIError.COMMON_UNKNOWN.description)) {
            return djiError.replace(DJIError.COMMON_UNKNOWN.description, "SDK错误，请联系<dev@dji.com>寻求帮助")
        }
        if (djiError.contains(DJIError.COMMON_UNDEFINED.description)) {
            return djiError.replace(DJIError.COMMON_UNDEFINED.description, "未定义的错误")
        }
        if (djiError.contains(DJIError.COMMON_TIMEOUT.description)) {
            return djiError.replace(DJIError.COMMON_TIMEOUT.description, "这个过程的执行已经超时")
        }
        if (djiError.contains(DJIError.COMMON_PARAM_ILLEGAL.description)) {
            return djiError.replace(DJIError.COMMON_PARAM_ILLEGAL.description, "参数非法")
        }
        if (djiError.contains(DJIError.COMMON_PARAM_INVALID.description)) {
            return djiError.replace(DJIError.COMMON_PARAM_INVALID.description, "参数无效")
        }
        if (djiError.contains(DJIError.COMMON_UNSUPPORTED.description)) {
            return djiError.replace(DJIError.COMMON_UNSUPPORTED.description, "不支持")
        }
        if (djiError.contains(DJIError.COMMON_DISCONNECTED.description)) {
            return djiError.replace(DJIError.COMMON_DISCONNECTED.description, "断开")
        }
        if (djiError.contains(DJIError.FIRMWARE_NON_SEQUENCE.description)) {
            return djiError.replace(DJIError.FIRMWARE_NON_SEQUENCE.description, "固件模式编号不连续")
        }
        if (djiError.contains(DJIError.FIRMWARE_LENGTH_WRONG.description)) {
            return djiError.replace(DJIError.FIRMWARE_LENGTH_WRONG.description, "固件长度无效")
        }
        if (djiError.contains(DJIError.FIRMWARE_CRC_WRONG.description)) {
            return djiError.replace(DJIError.FIRMWARE_CRC_WRONG.description, "固件crc值无效")
        }
        if (djiError.contains(DJIError.FLASH_CLEAR_WRONG.description)) {
            return djiError.replace(DJIError.FLASH_CLEAR_WRONG.description, "闪存清除错误的")
        }
        if (djiError.contains(DJIError.FLASH_WRITE_WRONG.description)) {
            return djiError.replace(DJIError.FLASH_WRITE_WRONG.description, "闪存写入错误")
        }
        if (djiError.contains(DJIError.UPDATE_WRONG.description)) {
            return djiError.replace(DJIError.UPDATE_WRONG.description, "更新错误")
        }
        if (djiError.contains(DJIError.FIRMWARE_MATCH_WRONG.description)) {
            return djiError.replace(DJIError.FIRMWARE_MATCH_WRONG.description, "固件匹配错误")
        }
        if (djiError.contains(DJIError.FLASH_FLUSHING.description)) {
            return djiError.replace(DJIError.FLASH_FLUSHING.description, "固件正在刷新")
        }
        if (djiError.contains(DJIError.MEDIA_INVALID_REQUEST_TYPE.description)) {
            return djiError.replace(DJIError.MEDIA_INVALID_REQUEST_TYPE.description, "媒体下载结果：媒体下载请求类型无效")
        }
        if (djiError.contains(DJIError.MEDIA_NO_SUCH_FILE.description)) {
            return djiError.replace(DJIError.MEDIA_NO_SUCH_FILE.description, "媒体下载结果：没有这样的文件")
        }
        if (djiError.contains(DJIError.MEDIA_REQUEST_CLIENT_ABORT.description)) {
            return djiError.replace(DJIError.MEDIA_REQUEST_CLIENT_ABORT.description, "媒体下载结果：客户端中止下载")
        }
        if (djiError.contains(DJIError.MEDIA_REQUEST_SERVER_ABORT.description)) {
            return djiError.replace(DJIError.MEDIA_REQUEST_SERVER_ABORT.description, "媒体下载结果：服务器中止下载")
        }
        if (djiError.contains(DJIError.MEDIA_REQUEST_DISCONNECT.description)) {
            return djiError.replace(DJIError.MEDIA_REQUEST_DISCONNECT.description, "媒体下载结果：下载链接断开")
        }
        if (djiError.contains(DJIError.IMAGE_TRANSMITTER_INVALID_PARAMETER.description)) {
            return djiError.replace(DJIError.IMAGE_TRANSMITTER_INVALID_PARAMETER.description, "输入参数无界限或无效")
        }
        if (djiError.contains(DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE.description)) {
            return djiError.replace(DJIError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE.description, "该命令不受当前固件版本的支持")
        }
        if (djiError.contains(DJIError.COMMAND_NOT_SUPPORTED_BY_HARDWARE.description)) {
            return djiError.replace(DJIError.COMMAND_NOT_SUPPORTED_BY_HARDWARE.description, "该命令不被当前硬件支持")
        }
        if (djiError.contains(DJIError.UNABLE_TO_GET_FIRMWARE_VERSION.description)) {
            return djiError.replace(DJIError.UNABLE_TO_GET_FIRMWARE_VERSION.description, "无法获取固件版本")
        }
        if (djiError.contains(DJIError.UNABLE_TO_GET_FLAGS.description)) {
            return djiError.replace(DJIError.UNABLE_TO_GET_FLAGS.description, "无法从服务器获取分析标志")
        }
        if (djiError.contains(DJIError.UNABLE_TO_GET_FLAG_BUT_RETRY.description)) {
            return djiError.replace(DJIError.UNABLE_TO_GET_FLAG_BUT_RETRY.description, "无法从服务器获取分析标志")
        }
        if (djiError.contains(DJIError.BATTERY_PAIR_FAILED.description)) {
            return djiError.replace(DJIError.BATTERY_PAIR_FAILED.description, "无法配对电池")
        }
        if (djiError.contains(DJIError.BATTERY_GET_SMART_BATTERY_INFO_FAILED.description)) {
            return djiError.replace(DJIError.BATTERY_GET_SMART_BATTERY_INFO_FAILED.description, "获取电池信息失败")
        }
        if (djiError.contains(DJIError.NO_NETWORK.description)) {
            return djiError.replace(DJIError.NO_NETWORK.description, "没有网络")
        }
        return if (djiError.contains(DJIError.DATABASE_IS_NOT_READY.description)) {
            djiError.replace(DJIError.DATABASE_IS_NOT_READY.description, "数据库没有就绪")
        } else djiError
    }

    companion object {
        private var mInstance: DJIErrorHelper? = null
        val instance: DJIErrorHelper?
            get() {
                if (null == mInstance) {
                    mInstance = DJIErrorHelper()
                }
                return mInstance
            }
    }
}