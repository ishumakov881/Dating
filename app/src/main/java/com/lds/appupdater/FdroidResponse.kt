package com.lds.appupdater

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
class FdroidResponse {
    @SerialName("apps")
    val apps: List<AppInfo> = emptyList()
}

@Serializable
data class AppInfo(
    val categories: List<String>,
    val suggestedVersionCode: String,
    val license: String,
    val name: String,
    val added: Long,
    val icon: String,
    val packageName: String,
    val lastUpdated: Long
)
