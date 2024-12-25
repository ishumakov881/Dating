package com.lds.appupdater

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class FdroidPackage(
    @SerializedName("versionName") val versionName: String,
    @SerializedName("versionCode") val versionCode: Int
)

// Модель данных для приложения
data class FdroidApp(
    @SerializedName("packageName") val packageName: String,

    )

// Общая структура ответа
data class FdroidResponse(
    @SerializedName("apps") val apps: List<FdroidApp> = emptyList(),
    @SerializedName("packages") val packages: Map<String, List<FdroidPackage>>
)


//@Serializable
//data class AppInfo(
//    val categories: List<String>,
//    val suggestedVersionCode: String,
//    val license: String,
//    val name: String,
//    val added: Long,
//    val icon: String,
//    val packageName: String,
//    val lastUpdated: Long
//)
