package com.lds.quickdeal.megaplan.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class TaskResponse(
    val meta: Meta,
    val data: List<FileData>
)

data class Meta(
    val status: Int,
    val errors: List<String>
)

data class FileData(
    val contentType: String,
    val id: String,
    val name: String,
    val mimeType: String,
    val extension: String,
    val size: Int,
    val timeCreated: String,
    val path: String,
    val possibleActions: List<String>,
    val metadata: Any? // Если структура metadata станет известна, замените Any на конкретный тип
)