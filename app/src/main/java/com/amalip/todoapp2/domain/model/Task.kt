package com.amalip.todoapp2.domain.model

import android.os.Parcelable
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

/**
 * Created by Amalip on 11/8/2021.
 */

@Entity
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val title: String = "",
    val description: String = "",
    val dateTime: LocalDateTime? = null,
    var status: Boolean = true

) : Parcelable