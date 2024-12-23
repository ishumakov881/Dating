package com.lds.quickdeal.android.db

import androidx.room.TypeConverter
import com.lds.quickdeal.android.entity.TaskStatus

class Converters {

    @TypeConverter
    fun fromTaskStatus(value: TaskStatus): String {
        return value.name // Сохраняем название enum как строку
    }

    @TypeConverter
    fun toTaskStatus(value: String): TaskStatus {
        return TaskStatus.valueOf(value)
    }
}