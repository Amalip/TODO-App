package com.amalip.todoapp2.core.di

import android.content.Context
import androidx.room.Room
import com.amalip.todoapp2.fremawork.db.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Amalip on 11/23/2021.
 */

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTaskDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, TaskDatabase::class.java, "Task").addMigrations().build()

}