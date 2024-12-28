package com.lds.quickdeal.android.config

import com.lds.quickdeal.android.db.ResponsibleWrapper

class Const {
    companion object {


        const val FDROID_URL = "https://fdroid.lds.online"
        const val FDROID_URL_INDEX = "$FDROID_URL/repo/index-v1.json"


        //Update App Feature
        //val UPDATE_SERVER_URL = "http://10.0.20.167"
        //val APK_UPDATE_URL = "http://10.0.20.167/release.apk"

        const val MEGAPLAN_SYSTEM_ID = "1000093"
        val MEGAPLAN_URL = "https://megaplan.lds.online";
        //var API_TASK = "/api/v3/task"

        val DB_NAME: String = "task_database_4"

        //Prod Server
        val API_URL = "http://94.158.46.179:90"
        var API_TASK = "/megaplan/task"

        //var API_UPLOAD = "/megaplan/upload"
        var API_UPLOAD = "/megaplan/task_v2"

        val FILE_KEY = "files"//npm


        //Local Server
//        val API_URL = "http://10.0.20.167";
//        var API_TASK = ""
//        var API_UPLOAD = ""
//        val FILE_KEY = "files[]" //php: files[]

        val PREF_NAME = "auth_prefs"

        //        val ldapHost = "ldsi.office.lds.ua"
//        //val ldapHost = "ldsii.office.lds.ua"
        val ldapHost = "office.lds.ua"//"aragorn.office.lds.ua"

        val ldapPort = 389 // LDAP
        val domain = "office" // NetBIOS-домен

        //val DEFAULT_RESPONSIBLE = OwnerWrapper("Employee", "1000161", "Гурьева Юлия Валерьевна")


        val DEFAULT_OWNERS_: List<ResponsibleWrapper> = listOf(
//            EmployeeWrapper(
//                contentType = "Employee",
//                id = "1000161",
//                description = "Гурьева Юлия Валерьевна"
//            ), // Default Owner
            //OwnerWrapper("Employee", "1000216", "Резниченко Иван Павлович"),
            //Owner("Employee", "1000163")
        )

        var UPLOAD_FILE_EXT = listOf(
            "jpg", "png",
            "doc", "docx", "pdf", "pem", "crt", "mp4", "apk", "zip", "rar", "xapk", "apk", "txt"
        )
    }
}


