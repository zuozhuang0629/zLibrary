package com.wxzt.kfly.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.wxzt.kfly.db.KFlyDataBase
import com.wxzt.kfly.db.entity.LocationTaskEntity
import com.wxzt.kfly.entitys.TaskDataEntity
import com.wxzt.kfly.enums.TaskModelEnum
import com.wxzt.kfly.enums.TaskStatusEnum
import com.wxzt.kfly.network.apiService
import com.wxzt.lib_common.viewmodel.BaseViewModel
import com.wxzt.lib_common.ext.request
import com.wxzt.lib_common.ext.room
import com.wxzt.lib_common.network.state.ResultState
import com.wyzt.lib_common.ext.utils.toInt

/**
 *  author : zuoz
 *  date : 2021/4/6 8:48
 *  description :
 */
class RequestTaskVM : BaseViewModel() {
    //服务数据
    var serviceTaskListResult = MutableLiveData<ResultState<TaskDataEntity>>()
    var locationTaskListResult = MutableLiveData<ResultState<List<LocationTaskEntity>>>()
//    val delTaskResult = MutableLiveData<ResultState<ResponseBody>>()

    /**
     * 获取任务列表
     *
     * @param taskType
     * @param status
     */
    fun getServiceTaskList(taskType: String, status: String, authorization: String) {
        request({ apiService.getMissionInfo(taskType, status, authorization) }, serviceTaskListResult, isShowDialog = true)
    }

    fun getLocationTaskList(userName: String, status: TaskStatusEnum, model: TaskModelEnum) {
        room({
            if (status == TaskStatusEnum.COMPLETE) {
                KFlyDataBase.getInstance().locationTaskDao().getLocationCompleteTasks(userName, status.toInt(), model.toInt())
            } else {
                KFlyDataBase.getInstance().locationTaskDao().getLocationNoCompleteTasks(userName, TaskStatusEnum.COMPLETE.toInt(), model.toInt())
            }

        }, locationTaskListResult, true)
    }


//    fun delServiceTask(taskid: String) {
//        val parameter: MutableMap<String, Any> = HashMap()
//        parameter["id"] = taskid
//        parameter["enable"] = true
//        val json: String = GsonUtils.toJson(parameter)
//        val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), json)
//        request({ apiService.updateMissionState(requestBody, appViewModel.userInfo.value!!.getRequestToken()) }, delTaskResult, isShowDialog = true, loadingMessage = "正在删除平台任务...")
//    }
}