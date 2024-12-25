package com.lds.appupdater

import android.content.Context
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.android.config.Const
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.serialization.gson.gson
import kotlinx.serialization.json.Json

class AppUpdater {

    companion object {

//        fun getCurrentAppVersion(context: Context): String {
//            val packageInfo: PackageInfo =
//                context.packageManager.getPackageInfo(context.packageName, 0)
//            return packageInfo.versionName ?: BuildConfig.VERSION_NAME
//        }

        fun getCurrentAppVersion(context: Context): String {
            return BuildConfig.VERSION_NAME
        }

        fun getCurrentAppVersionCode(context: Context): Int {
            return BuildConfig.VERSION_CODE
        }


//        fun isNewVersionAvailable(currentVersion: String, serverVersion0: String): Boolean {
//            return currentVersion != serverVersion0
//        }

        fun isNewVersionAvailable(currentVersionCode: Int, serverVersionCode: Int): Boolean {
            return serverVersionCode > currentVersionCode
        }

        suspend fun checkAppVersion(context: Context): Int {
            val packageName = context.packageName
            val client = HttpClient() {
                install(ContentNegotiation) {
                    gson()

//                    json(Json {
//                        prettyPrint = true
//                        isLenient = true
////                        encodeDefaults = true
////                        ignoreUnknownKeys = true
//                    })
                }
            }
            return try {
//                val response: String = client.get(Const.FDROID_URL).bodyAsText()
//                val appResponse = Json.decodeFromString<FdroidResponse>(response)
//                val appInfo = appResponse.apps.find { it.packageName == packageName }
//                appInfo?.suggestedVersionCode?.toInt() ?: -1

                val appResponse: FdroidResponse = client.get(Const.FDROID_URL).body()
                val appInfo = appResponse.apps.find { it.packageName == packageName }
                appInfo?.suggestedVersionCode?.toInt() ?: -1
            } catch (e: Exception) {
                e.printStackTrace()
                -1
            } finally {
                client.close()
            }
        }


//        suspend fun downloadApk(apkUrl: String, outputFile: File) {
//            val client = HttpClient() {
//                install(Logging) {
//                    level = LogLevel.BODY
//                }
//            }
//            try {
//                val bytes = client.get(apkUrl).body<ByteArray>()
//                outputFile.writeBytes(bytes)
//            } finally {
//                client.close()
//            }
//        }


//        fun installApk(context: Context, apkFile: File) {
//            try {
//                val uri =
//                    FileProvider.getUriForFile(context, "${context.packageName}.provider", apkFile)
//                val intent = Intent(Intent.ACTION_VIEW).apply {
//                    setDataAndType(uri, "application/vnd.android.package-archive")
//                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                }
//                context.startActivity(intent)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }

//        suspend fun checkAppVersion(serverUrl: String): AppVersionInfo? {
//            val client = HttpClient() {
//                install(ContentNegotiation) {
//                    gson()
//                }
//            }
//            return try {
//                client.get("$serverUrl/version/version_info.json").body()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                if (BuildConfig.DEBUG) {
//                    println("@@@ " + e.message)
//                }
//                null
//            } finally {
//                client.close()
//            }
//        }
//    }
    }
}
