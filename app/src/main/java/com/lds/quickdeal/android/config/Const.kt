package com.lds.quickdeal.android.config

import com.lds.quickdeal.megaplan.entity.Owner
import kotlinx.serialization.Serializable

class Const {
    companion object {

        //val API_URL = "https://megaplan.lds.online";
        //var API_TASK = "/api/v3/task"

        val API_URL = "http://10.0.20.179:90";
        var API_TASK = "/megaplan/task"
        //var API_UPLOAD = "/megaplan/upload"
        var API_UPLOAD = "/megaplan/task_v2"

        val FILE_KEY = "files" //php[]
//        val API_URL = "http://10.0.20.167";
//        var API_TASK = ""
//        var API_UPLOAD = ""


        val PREF_NAME = "auth_prefs"


        val ldapHost = "ldsi.office.lds.ua" // или "ldsii.office.lds.ua"
        val ldapPort = 389 // LDAP
        val domain = "office" // NetBIOS-домен

        //val DEFAULT_RESPONSIBLE = OwnerWrapper("Employee", "1000161", "Гурьева Юлия Валерьевна")


        val DEFAULT_OWNERS: List<OwnerWrapper> = listOf(
//        OwnerWrapper(
//            contentType = "Employee",
//            id = "1000161",
//            description = "Гурьева Юлия Валерьевна"
//        ), // Default Owner
            //OwnerWrapper("Employee", "1000216", "Резниченко Иван Павлович"),
        //Owner("Employee", "1000163")
        )

        var UPLOAD_FILE_EXT = kotlin.collections.listOf("jpg", "png",
            "doc", "docx", "pdf", "pem", "crt", "mp4", "apk", "zip", "rar", "xapk", "apk")
    }




}

@Serializable
data class OwnerWrapper(
    val contentType: String = "Employee", // всегда Employee
    val id: String, // Идентификатор
    val description: String
)