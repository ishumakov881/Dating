package com.lds.quickdeal.android.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tasks")
data class UploaderTask(
    @PrimaryKey(autoGenerate = true) var _id: Long = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "subject") var subject: String,
    @ColumnInfo(name = "is_urgent") val isUrgent: Boolean,
    //@ColumnInfo(name = "is_completed") val completed: Boolean = false,

    @ColumnInfo(name = "status") var status: TaskStatus,

    @ColumnInfo(name = "created_ut") val createdAt: String,
    @ColumnInfo(name = "updatedAt") val updatedAt: String,
    @ColumnInfo(name = "megaplanId") var megaplanId: String

) {
    fun isNewTask(): Boolean {
        return _id <= 0
    }
}