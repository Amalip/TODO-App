package com.amalip.todoapp2.core.di

import com.amalip.todoapp2.data.source.TaskRepositoryImpl
import com.amalip.todoapp2.domain.repository.TaskRepository
import com.amalip.todoapp2.fremawork.db.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Amalip on 11/23/2021.
 */

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(taskDatabase: TaskDatabase): TaskRepository =
        TaskRepositoryImpl(taskDatabase.taskDao())

}