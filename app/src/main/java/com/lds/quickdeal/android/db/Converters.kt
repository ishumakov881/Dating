package com.lds.quickdeal.android.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.lds.quickdeal.megaplan.entity.Owner
import com.lds.quickdeal.megaplan.entity.Responsible

import com.lds.quickdeal.megaplan.entity.TaskStatus

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromTaskStatus(value: TaskStatus): String {
        return value.name // Сохраняем название enum как строку
    }

    @TypeConverter
    fun toTaskStatus(value: String): TaskStatus {
        return TaskStatus.valueOf(value)
    }

    @TypeConverter
    fun fromResponsible(responsible: Responsible?): String? {
        return gson.toJson(responsible)
    }

    @TypeConverter
    fun toResponsible(data: String?): Responsible? {
        return data?.let {
            gson.fromJson(it, Responsible::class.java)
        }
    }

    @TypeConverter
    fun fromOwner(owner: Owner?): String? {
        return gson.toJson(owner)
    }

    @TypeConverter
    fun toOwner(data: String?): Owner? {
        return data?.let {
            gson.fromJson(it, Owner::class.java)
        }
    }
}