package com.lds.quickdeal.android.utils

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.content.res.Resources
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Build
import android.telephony.TelephonyManager
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.megaplan.entity.TaskRequest
import io.ktor.client.request.forms.FormBuilder
import java.util.Locale
import java.util.TimeZone

class TaskUtils {
    companion object{

        fun FormBuilder.appendTaskRequest(context: Context, taskRequest: TaskRequest) {
//                        append("json", kotlinx.serialization.json.Json.encodeToString(TaskRequest.serializer(),taskRequest), Headers.build {
//                            append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
//                            //append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
//                        })
//                        append(
//                            key = "json",
//                            value = kotlinx.serialization.json.Json.encodeToString(TaskRequest.serializer(),taskRequest),
//                            headers = Headers.build {
//                                append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
//                            }
//                        )

            val deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}" // Пример: Samsung Galaxy S10
            val osVersion = "Android ${Build.VERSION.RELEASE}" // Пример: Android 12
            val timeZone = TimeZone.getDefault().id // Пример: Europe/Moscow

            taskRequest.latitude?.let { append("latitude", it) }
            taskRequest.longitude?.let { append("longitude", it) }

            taskRequest.name?.let { append("name", it) }
            taskRequest.subject?.let { append("subject", it) }
            append("contentType", "Task")
            append("isTemplate", "false")
            append("isUrgent", "false")
            append("responsibleContentType", "Employee")
            taskRequest.responsible?.let { append("responsibleId", it.id) }

            append("app_version", BuildConfig.VERSION_NAME) // Версия приложения
            append("device_model", deviceModel) // Модель устройства
            append("os_version", osVersion) // Версия ОС
            append("time_zone", timeZone) // Часовой пояс
            val deviceId = Build.SERIAL.takeIf { it.isNotEmpty() } ?: "unknown"
            append("device_id", deviceId)

            val language = Locale.getDefault().language // Например, "en", "ru"
            append("language", language)
            val region = Locale.getDefault().country // Например, "US", "RU"
            append("region", region)

            val displayMetrics = Resources.getSystem().displayMetrics
            val screenResolution =
                "${displayMetrics.widthPixels}x${displayMetrics.heightPixels}"
            append("screen_resolution", screenResolution)

            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkType =
                connectivityManager.activeNetworkInfo?.typeName ?: "unknown"
            append("network_type", networkType)

            val batteryIntent = context.registerReceiver(
                null,
                IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            )
            val batteryLevel =
                batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            append("battery_level", batteryLevel.toString())
            val timestamp = System.currentTimeMillis().toString()
            append("timestamp", timestamp)

            val isTablet =
                (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
            val deviceType = if (isTablet) "tablet" else "phone"
            append("device_type", deviceType)

            val apiLevel = Build.VERSION.SDK_INT
            append("api_level", apiLevel.toString())
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val carrierName = telephonyManager.networkOperatorName ?: "unknown"
            append("carrier_name", carrierName)
        }
    }
}