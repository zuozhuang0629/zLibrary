package com.wxzt.kfly.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.wxzt.kfly.R
import com.wxzt.kfly.adapter.TaskLocationAdapter
import com.wxzt.kfly.adapter.TaskServiceAdapter
import com.wxzt.kfly.appViewModel
import com.wxzt.kfly.databinding.FragmentTaskManagementBinding
import com.wxzt.kfly.db.entity.LocationTaskEntity
import com.wxzt.kfly.entitys.TabEntity
import com.wxzt.kfly.entitys.TaskData
import com.wxzt.kfly.enums.TaskModelEnum
import com.wxzt.kfly.enums.TaskStatusEnum
import com.wxzt.kfly.enums.TaskTypeEnum
import com.wxzt.kfly.ext.initData
import com.wxzt.kfly.ext.isLoginAndStart
import com.wxzt.kfly.ext.showTitleDialog
import com.wxzt.kfly.ui.activity.TaskLocationDetails
import com.wxzt.kfly.ui.activity.planning.PlanningActivity
import com.wxzt.kfly.viewmodel.request.RequestTaskVM
import com.wxzt.lib_common.base.fragment.BaseVBFragment
import com.wxzt.lib_common.ext.parseState
import com.wxzt.lib_common.ext.init
import com.wxzt.lib_common.ext.startActivity
import com.wxzt.lib_common.ext.visibility

import java.util.*

/**
 *  author : zuoz
 *  date : 2021/4/2 15:22
 *  description : 任务管理
 */
class TaskManagementFragment : BaseVBFragment<FragmentTaskManagementBinding>() {

    private val mViewModel: RequestTaskVM by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(RequestTaskVM::class.java)
    }

    private val locationAdapter: TaskLocationAdapter by lazy { TaskLocationAdapter() }
    private val serviceAdapter: TaskServiceAdapter by lazy { TaskServiceAdapter() }

    private var taskModelEnum: TaskModelEnum = TaskModelEnum.POLYGON
    private var taskStatusEnum: TaskStatusEnum = TaskStatusEnum.NO_EXECUTE
    private var taskTypeEnum: TaskTypeEnum = TaskTypeEnum.LOCATION
    override fun getViewBinding(): FragmentTaskManagementBinding = FragmentTaskManagementBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        initTabLayout()
        initSpinner()
        initAdapter()
        initRefreshLayout()
    }

    override fun initData() {
    }

    override fun initListener() {
        locationAdapter.setOnItemChildClickListener { adapter, view, position ->
            val data = serviceAdapter.data[position]
            when (view.id) {
                R.id.item_img -> {
                    requireContext().startActivity<TaskLocationDetails>()
                }
            }
        }
        serviceAdapter.setOnItemChildClickListener { adapter, view, position ->
            val data = serviceAdapter.data[position]
            when (view.id) {
                R.id.task_service_ll -> {
                }
                R.id.task_fly -> {
                    showTitleDialog("确定执行任务:${data.flyTaskName}", positiveText = "执行") {
                        it.dismiss()
                        startActivity(data)
                    }
                }
            }
//
        }

        mViewModel.serviceTaskListResult.observe(viewLifecycleOwner) {
            parseState(it, { data ->
                data.body?.let { body ->
                    serviceAdapter.setList(body.resultData.toList())
                    setRefreshShow(false)
                } ?: let {
                    setRefreshShow(false)
                    serviceAdapter.setList(mutableListOf())
                    serviceAdapter.setEmptyView(R.layout.layout_empty)
                }
            }, {
                setRefreshShow(false)
                serviceAdapter.setList(mutableListOf())
                serviceAdapter.setEmptyView(R.layout.layout_error)
            }, {
                setRefreshShow(true)
            })
        }


        mViewModel.locationTaskListResult.observe(viewLifecycleOwner) {
            parseState(it, { locationData ->
                locationAdapter.setEmptyView(R.layout.layout_empty)
                locationAdapter.setList(locationData)
                setRefreshShow(false)
            }, {
                setRefreshShow(false)
                locationAdapter.data.clear()
//                locationAdapter.setEmptyView(R.layout.layout_error)
            }, {
                setRefreshShow(true)
            })
        }
    }

    private fun initTabLayout() {
        val titles = ArrayList<CustomTabEntity>()
        titles.add(TabEntity("本地任务", 0, 0))
        titles.add(TabEntity("平台任务", 0, 0))

        mViewBinding.taskTablayout.setTabData(titles)
        mViewBinding.taskTablayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                mViewBinding.taskSwipeRefresh.isRefreshing = true
                if (position == 0) {
                    mViewBinding.taskStatus.visibility(true)
                    taskTypeEnum = TaskTypeEnum.LOCATION
                    refreshLocation()
                } else {
                    taskTypeEnum = TaskTypeEnum.SERVICE
                    mViewBinding.taskStatus.visibility(false)
                    refreshService()
                }

                setAdapter(position)
            }

            override fun onTabReselect(position: Int) {
            }

        })
    }

    private fun initAdapter() {
        mViewBinding.taskRecyclerView.init(GridLayoutManager(requireContext(), 3), locationAdapter)
        locationAdapter.addChildClickViewIds(R.id.item_img)
        serviceAdapter.addChildClickViewIds(R.id.task_fly)
    }

    private fun initSpinner() {
        val taskStatus: List<String> = LinkedList(listOf("未完成", "已完成"))
        mViewBinding.taskStatus.initData(taskStatus) { position, title ->
            taskStatusEnum = if (position == 0) {
                TaskStatusEnum.NO_EXECUTE
            } else {
                TaskStatusEnum.COMPLETE
            }
        }
        mViewBinding.taskStatus.attachDataSource(taskStatus)
        mViewBinding.taskStatus.setOnSpinnerItemSelectedListener { parent, view, position, id ->

            taskStatusEnum = if (position == 0) {
                TaskStatusEnum.NO_EXECUTE
            } else {
                TaskStatusEnum.COMPLETE
            }
            refreshData()
        }

        val taskType: List<String> = LinkedList(listOf(TaskModelEnum.POLYGON.getName(),
                TaskModelEnum.OBLIQUE.getName(), TaskModelEnum.PANORAMIC.getName(), TaskModelEnum.FIX.getName()))
        mViewBinding.taskModel.initData(taskType) { position, title ->
            taskModelEnum = when (title) {
                TaskModelEnum.POLYGON.getName() -> TaskModelEnum.POLYGON
                TaskModelEnum.OBLIQUE.getName() -> TaskModelEnum.OBLIQUE
                TaskModelEnum.PANORAMIC.getName() -> TaskModelEnum.POLYGON
                TaskModelEnum.FIX.getName() -> TaskModelEnum.FIX
                else -> TaskModelEnum.POLYGON
            }
        }

        mViewBinding.taskModel.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            taskModelEnum = if (position == 0) {
                TaskModelEnum.POLYGON
            } else if (position == 1) {
                TaskModelEnum.OBLIQUE
            } else if (position == 2) {
                TaskModelEnum.PANORAMIC
            } else {
                TaskModelEnum.FIX
            }

            refreshData()
        }
    }

    private fun initRefreshLayout() {
        mViewBinding.taskSwipeRefresh.setColorSchemeColors(Color.rgb(159, 202, 61))
        serviceAdapter.loadMoreModule.isEnableLoadMore = false
        locationAdapter.setEmptyView(R.layout.layout_empty)
        serviceAdapter.setEmptyView(R.layout.layout_empty)

        refreshLocation()
        mViewBinding.taskSwipeRefresh.setOnRefreshListener {
            when (taskTypeEnum) {
                TaskTypeEnum.LOCATION -> {
                    refreshLocation()
                }
                TaskTypeEnum.SERVICE -> {
                    refreshService()
                }
            }
        }
    }

    /**
     * 刷新服务数据
     */
    private fun refreshService() {
        setRefreshShow(true)
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        serviceAdapter.loadMoreModule.isEnableLoadMore = false

        appViewModel.userInfo.value?.let {
            mViewModel.getServiceTaskList(taskModelEnum.vlaue, taskStatusEnum.vlaue, it.getRequestToken())
        } ?: let {
            setRefreshShow(false)
        }
    }

    /**
     *  刷新本地数据
     *
     */
    private fun refreshLocation() {
        setRefreshShow(true)
        locationAdapter.loadMoreModule.isEnableLoadMore = false
        appViewModel.userInfo.value?.let {
            mViewModel.getLocationTaskList(it.userName, taskStatusEnum, taskModelEnum)
        } ?: let {
            setRefreshShow(false)
            locationAdapter.setEmptyView(R.layout.layout_error)
            ToastUtils.showLong("请登录账号")
        }
    }

    private fun setRefreshShow(isShow: Boolean) {
        mViewBinding.taskSwipeRefresh.isRefreshing = isShow
    }

    /**
     * 设置显示的adapter
     *
     * @param position 0：本地 1：平台
     */
    private fun setAdapter(position: Int) {
        if (position == 0) {
            mViewBinding.taskRecyclerView.init(GridLayoutManager(requireContext(), 3), locationAdapter)
        } else {
            mViewBinding.taskRecyclerView.init(LinearLayoutManager(requireContext()), serviceAdapter)
        }
    }

    private fun startActivity(data: TaskData) {
        appViewModel.taskInfo.value = data.getLocationData(appViewModel.userInfo.value!!.userName,
                taskModelEnum, TimeUtils.getNowDate())
        requireContext().startActivity<PlanningActivity>()

    }

    /**
     * 刷新数据
     *
     */
    private fun refreshData() {
        when (taskTypeEnum) {
            TaskTypeEnum.LOCATION -> {
                refreshLocation()
            }
            TaskTypeEnum.SERVICE -> {
                refreshService()
            }
        }
    }

}