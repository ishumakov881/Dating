package com.lds.quickdeal.android.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "responsible",
    indices = [Index(value = ["megaplanUserId"], unique = true)]
)
@Serializable
data class ResponsibleWrapper(
    @PrimaryKey(autoGenerate = true)
    var _id: Long = 0,

    val contentType: String = "Employee",

    val megaplanUserId: String,

    val description: String,

    val avatar: String,

    val position: String
)