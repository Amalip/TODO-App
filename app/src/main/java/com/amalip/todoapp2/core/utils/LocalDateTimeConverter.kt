package com.amalip.todoapp2.core.utils

import androidx.room.TypeConverter
import java.time.LocalDateTime

/**
 * Created by Amalip on 11/23/2021.
 */

object LocalDateTimeConverter {

    @TypeConverter
    fun toDateTime(dateString: String?): LocalDateTime? {
        return if (dateString == null) null else LocalDateTime.parse(dateString)
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime?): String? {
        return date?.toString()
    }

}