package com.lds.quickdeal.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lds.appupdater.AppUpdater.Companion.checkAppVersion
import com.lds.appupdater.AppUpdater.Companion.getCurrentAppVersionCode
import com.lds.appupdater.AppUpdater.Companion.isNewVersionAvailable
import com.lds.quickdeal.R
import com.lds.quickdeal.android.config.Const.Companion.APK_UPDATE_URL
import com.lds.quickdeal.android.config.Const.Companion.UPDATE_SERVER_URL
import com.lds.quickdeal.navigation.AppNavigation
import com.lds.quickdeal.ui.viewmodels.MainViewModel

import com.pouyaheydari.appupdater.compose.AndroidAppUpdater
import com.pouyaheydari.appupdater.compose.pojo.UpdaterDialogData
import com.pouyaheydari.appupdater.core.pojo.Store
import com.pouyaheydari.appupdater.core.pojo.StoreListItem
import com.pouyaheydari.appupdater.core.pojo.Theme
import dagger.hilt.android.AndroidEntryPoint


//@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            MaterialTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    LoginScreen()
//                }
//            }
//        }
//    }
//}


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

//            val viewModel: MainViewModel = hiltViewModel()
//            val isUpdateDialogVisible by viewModel.isUpdateDialogVisible.collectAsState()
//            val storeListItems by viewModel.storeListItem.collectAsState()


            MaterialTheme {
                var context: Context = LocalContext.current
                var isUpdateDialogVisible by remember { mutableStateOf(false) }
                var storeListItems = listOf(
                    StoreListItem(
                        store = Store.DIRECT_URL,
                        title = "Обновить",
                        url = APK_UPDATE_URL
                    )
                )
                LaunchedEffect(Unit) {
                    isUpdateDialogVisible = false

                    val appVersionInfo = checkAppVersion(UPDATE_SERVER_URL)
                    println("###AppVerison: $appVersionInfo")
                    val currentVersionCode = getCurrentAppVersionCode(context)

                    /*appVersionInfo != null && appVersionInfo.versionName != null && isNewVersionAvailable(
                            currentVersion, appVersionInfo.versionName*/

                    if (appVersionInfo != null) {
                        var isNew =
                            isNewVersionAvailable(currentVersionCode, appVersionInfo.versionCode ?: 0)
                        println("Current: ${currentVersionCode}, New version: ${appVersionInfo.versionCode} $isNew")

                        if (isNew) {
//                    _storeListItem.value = listOf(
//                        StoreListItem(
//                            store = Store.DIRECT_URL,
//                            title = if (BuildConfig.DEBUG) "Обновить: ${appVersionInfo.versionCode}" else "Обновить",
//                            url = APK_UPDATE_URL
//                        )
//                    )
                            isUpdateDialogVisible = true
                        }
                    }
                }
                if (isUpdateDialogVisible) {
                    AndroidAppUpdater(
                        UpdaterDialogData(

                            dialogTitle = stringResource(id = R.string.app_name),
                            dialogDescription = "Доступна новая версия приложения. Хотите обновить?",
                            storeList = storeListItems,
                            theme = Theme.SYSTEM_DEFAULT,
                            onDismissRequested = {
                                isUpdateDialogVisible = false //viewModel.dismissUpdateDialog()
                            }
                        ),
                    )
                }
                AppNavigation()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        print("@@@@ $requestCode $permissions $grantResults")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        print("@@@@ $requestCode $permissions $grantResults")
    }

}
//    @Composable
//    private fun AppUpdaterDialog() {
//
//        val coroutineScope = rememberCoroutineScope()
//        var showUpdateDialog by remember { mutableStateOf(false) }
//        var isUpdating by remember { mutableStateOf(false) }
//
//        LaunchedEffect(Unit) {
//
//            val appVersionInfo = checkAppVersion(UPDATE_SERVER_URL)
//            println("###AppVerison: $appVersionInfo")
//            val currentVersion = getCurrentAppVersion(this@MainActivity)
//
//            if (appVersionInfo != null && appVersionInfo.versionName != null && isNewVersionAvailable(
//                    currentVersion,
//                    appVersionInfo.versionName
//                )
//            ) {
//                showUpdateDialog = true
//            }
//        }
//
//        if (showUpdateDialog) {
//            UpdateDialog(
//                onUpdateClick = {
//                    isUpdating = true
//
//                    coroutineScope.launch {// Calls to launch should happen inside a LaunchedEffect and not composition
//                        val apkFile = File(cacheDir, "app-update.apk")
//
//                        try {
//                            downloadApk(APK_UPDATE_URL, apkFile)
//                            installApk(this@MainActivity, apkFile)
//                        } catch (e: Exception) {
//                            println("${e.message}")
//                        }
//
//                        isUpdating = false
//                        showUpdateDialog = false
//                    }
//                },
//                onDismiss = { showUpdateDialog = false },
//                isUpdating = isUpdating
//            )
//        }
//    }