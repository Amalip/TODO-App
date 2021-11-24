package com.amalip.todoapp2.fremawork.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amalip.todoapp2.core.utils.LocalDateTimeConverter
import com.amalip.todoapp2.data.dao.TaskDao
import com.amalip.todoapp2.domain.model.Task

/**
 * Created by Amalip on 11/23/2021.
 */

@Database(entities = [Task::class], version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class TaskDatabase() : RoomDatabase() {

    abstract fun taskDao(): TaskDao

}