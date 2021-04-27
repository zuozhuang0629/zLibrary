package com.wxzt.kfly.db

import androidx.room.*
import com.wxzt.kfly.db.entity.LocationTaskEntity


/**
 *  author      : zuoz
 *  date        : 2021/4/6 11:11
 *  description :
 */
@Dao
interface LocationTaskDao {

    /**
     * 获取本地完成任务
     *
     * @param user      用户
     * @param status    状态
     * @param model     行模式
     * @return
     */
    @Query("select * from task where user_name=:user and task_status=:status and task_model=:model")
    suspend fun getLocationCompleteTasks(user: String, status: Int, model: Int): List<LocationTaskEntity>

    /**
     * 获取本地未完成任务
     *
     * @param user      用户
     * @param status    状态
     * @param model     行模式
     * @return
     */
    @Query("select * from task where user_name=:user and task_status!=:status and task_model=:model")
    suspend fun getLocationNoCompleteTasks(user: String, status: Int, model: Int): List<LocationTaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocationTask(locationTaskEntity: LocationTaskEntity): Long

    @Update
    suspend fun updateLocationTask(vararg locationTaskEntity: LocationTaskEntity)
}