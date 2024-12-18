package com.lds.quickdeal.android.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "subject") val subject: String,
    @ColumnInfo(name = "is_urgent") val isUrgent: Boolean,
    @ColumnInfo(name = "is_completed") val completed: Boolean = false,



    @ColumnInfo(name = "created_ut") val createdAt: String,
    @ColumnInfo(name = "updatedAt") val updatedAt: String

)