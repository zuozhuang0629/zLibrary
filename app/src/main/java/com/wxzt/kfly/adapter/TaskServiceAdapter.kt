package com.wxzt.kfly.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.wxzt.kfly.R
import com.wxzt.kfly.entitys.TaskData

/**
 *  author : zuoz
 *  date : 2021/4/6 10:16
 *  description :同步任务
 */
class TaskServiceAdapter : BaseQuickAdapter<TaskData, BaseViewHolder>(R.layout.item_task_service), LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: TaskData) {
        holder.setText(R.id.tv_task_name,item.flyTaskName)
    }
}