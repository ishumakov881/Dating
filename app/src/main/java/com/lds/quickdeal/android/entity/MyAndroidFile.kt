//package com.lds.quickdeal.android.entity
//
//import android.net.Uri
//import androidx.core.net.toFile
//import com.darkrockstudios.libraries.mpfilepicker.MPFile
//
//
//@Parcelize
//public data class MyAndroidFile(
//    override val path: String,
//    override val platformFile: Uri,
//) : MPFile<Uri> {
//    override suspend fun getFileByteArray(): ByteArray = platformFile.toFile().readBytes()
//}