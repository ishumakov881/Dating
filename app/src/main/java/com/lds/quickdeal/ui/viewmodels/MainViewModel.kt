package com.lds.quickdeal.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lds.appupdater.AppUpdater.Companion.checkAppVersion
import com.lds.appupdater.AppUpdater.Companion.getCurrentAppVersion
import com.lds.appupdater.AppUpdater.Companion.getCurrentAppVersionCode
import com.lds.appupdater.AppUpdater.Companion.isNewVersionAvailable
import com.lds.quickdeal.BuildConfig
import com.lds.quickdeal.android.config.Const.Companion.APK_UPDATE_URL
import com.lds.quickdeal.android.config.Const.Companion.UPDATE_SERVER_URL
import com.pouyaheydari.appupdater.core.pojo.Store
import com.pouyaheydari.appupdater.core.pojo.StoreListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    context: Context
) : ViewModel() {

    private val _storeListItem = MutableStateFlow<List<StoreListItem>>(
        listOf(
            StoreListItem(
                store = Store.DIRECT_URL,
                title = "Обновить",
                url = APK_UPDATE_URL
            )
        )
    )
    val storeListItem: StateFlow<List<StoreListItem>> get() = _storeListItem


    private val _isUpdateDialogVisible = MutableStateFlow(false)
    val isUpdateDialogVisible: StateFlow<Boolean> = _isUpdateDialogVisible

    fun showUpdateDialog() {
        _isUpdateDialogVisible.value = true
    }

    fun dismissUpdateDialog() {
        _isUpdateDialogVisible.value = false
    }


    init {
        checkForUpdate(context)
    }

    fun checkForUpdate(context: Context) {
        viewModelScope.launch {

            dismissUpdateDialog()

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
                    showUpdateDialog()
                }
            }


        }
    }

}
