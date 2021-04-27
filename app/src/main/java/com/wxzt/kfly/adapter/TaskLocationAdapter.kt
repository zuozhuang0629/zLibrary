package com.wxzt.kfly.adapter

import android.widget.ImageView
import com.blankj.utilcode.util.TimeUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.wxzt.kfly.KFlyApplication
import com.wxzt.kfly.R
import com.wxzt.kfly.db.entity.LocationTaskEntity

/**
 *  author : zuoz
 *  date : 2021/4/2 18:09
 *  description : 本地任务适配器
 */
class TaskLocationAdapter : BaseQuickAdapter<LocationTaskEntity, BaseViewHolder>(R.layout.item_task_location), LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: LocationTaskEntity) {
        holder.setText(R.id.item_time, TimeUtils.date2String(item.createTime, TimeUtils.getSafeDateFormat("yyyy-MM-dd")))
        val imageView = holder.getView<ImageView>(R.id.item_img)
        Glide.with(KFlyApplication.app)
                .load(item.imgPath)
                .placeholder(R.drawable.lost)
                .error(R.drawable.lost)
                .into(imageView)
    }
}