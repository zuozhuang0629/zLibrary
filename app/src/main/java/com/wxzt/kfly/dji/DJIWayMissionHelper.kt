package com.wxzt.kfly.dji

import com.wxzt.kfly.dji.ICommonCallBack.ICommonCallBackWith
import com.wxzt.kfly.dji.ICommonCallBack.ICommonResultCallBackWith
import dji.common.error.DJIError
import dji.common.mission.waypoint.*
import dji.sdk.mission.MissionControl
import dji.sdk.mission.waypoint.WaypointMissionOperatorListener

/**
 * Copyright (C) 湖北无垠智探科技发展有限公司
 * Author:   zuoz
 * Date:     2019/10/11 8:55
 * Description: 任务帮助类
 * History:
 */
class DJIWayMissionHelper {
    /**
     * @param djiMission
     */
    fun LoadMission(djiMission: WaypointMission?): DJIError? {
        if (null == djiMission) {
            return null
        }
        val missionOperator = MissionControl.getInstance().waypointMissionOperator
                ?: return DJIError.COMMON_TIMEOUT
        return missionOperator.loadMission(djiMission)
        //        return DJIError.COMMON_TIMEOUT;
    }

    fun uploadTask() {
        val missionOperator = MissionControl.getInstance().waypointMissionOperator


        missionOperator.uploadMission { }
    }

    fun startWayMission() {
        val missionOperator = MissionControl.getInstance().waypointMissionOperator
                ?: return
        missionOperator.startMission { djiError ->
            if (null != djiError) {
//                    ToastUtil.showToast("开始任务错误：" + djiError.toString());
            }
        }
    }

    fun pauseMission(iCommonCallBackWith: ICommonCallBackWith<DJIError?>) {
        val missionOperator = MissionControl.getInstance().waypointMissionOperator
                ?: return
        missionOperator.pauseMission { djiError ->
            iCommonCallBackWith.onCallBack(djiError)
            //                ToastUtil.showToast(djiError.toString());
        }
    }

    fun resumeMission() {
        val missionOperator = MissionControl.getInstance().waypointMissionOperator
                ?: return
        missionOperator.resumeMission { djiError ->
            println(djiError)
            //                ToastUtil.showToast(djiError.toString());
        }
    }

    fun overMissionAndGoHome() {
        val missionOperator = MissionControl.getInstance().waypointMissionOperator
                ?: return
        missionOperator.stopMission {
            DJIDeviceHelper.instance.startGoHome {
                it?.let {

                }
            }
        }
    }

    companion object {
        private var mInstance: DJIWayMissionHelper? = null
        val instance: DJIWayMissionHelper
            get() {
                if (null == mInstance) {
                    mInstance = DJIWayMissionHelper()
                }
                return mInstance!!
            }

        /**
         * 任务点拍照
         *
         * @param waypoint
         */
        fun ShootPhoto(waypoint: Waypoint) {
            waypoint.addAction(WaypointAction(WaypointActionType.START_TAKE_PHOTO, 1))
            waypoint.addAction(WaypointAction(WaypointActionType.STAY, 500))
        }

        fun startVideo(waypoint: Waypoint) {
            waypoint.addAction(WaypointAction(WaypointActionType.START_RECORD, 1))
            waypoint.addAction(WaypointAction(WaypointActionType.STAY, 500))
        }

        fun stopVideo(waypoint: Waypoint) {
            waypoint.addAction(WaypointAction(WaypointActionType.STOP_RECORD, 1))
            waypoint.addAction(WaypointAction(WaypointActionType.STAY, 500))
        }
    }
}