package com.lds.quickdeal.megaplan.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Avatar(
    @SerialName("contentType")
    val contentType: String,

    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("extension")
    val extension: String,

    @SerialName("size")
    val size: Long,

    @SerialName("path")
    val path: String,

    @SerialName("thumbnail")
    val thumbnail: String?
)