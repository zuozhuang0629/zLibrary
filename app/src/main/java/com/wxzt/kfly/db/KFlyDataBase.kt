package com.wxzt.kfly.db

import androidx.room.*
import com.wxzt.kfly.KFlyApplication
import com.wxzt.kfly.db.converters.*


import com.wxzt.kfly.db.entity.LocationTaskEntity


/**
 * @Author:          zuoz
 * @CreateDate:     2021/1/28 17:43
 * @Description:    数据库base
 */

@Database(
        entities = [LocationTaskEntity::class],
        version = 1,
        exportSchema = false
)

@TypeConverters(
        DateConverters::class,
        ListConverters::class,
        FlySettingConverters::class,
        TaskModelConverters::class,
        TaskStatusConverters::class,
        TaskTypeConverters::class
)
abstract class KFlyDataBase : RoomDatabase() {
    abstract fun locationTaskDao(): LocationTaskDao
//    abstract fun budgetDao(): BudgetCategoryDao

    companion object {
        private var INSTANCE: KFlyDataBase? = null

        fun getInstance(): KFlyDataBase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                        KFlyApplication.app,
                        KFlyDataBase::class.java,
                        "kfly_master.db"
                ).build().also {
                    INSTANCE = it
                }

            }
        }

    }
}