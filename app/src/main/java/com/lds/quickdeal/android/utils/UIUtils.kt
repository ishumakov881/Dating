package com.lds.quickdeal.android.utils

import com.lds.quickdeal.android.config.Const

object UIUtils {


    fun getAvatarUrl(thumbnail: String) : String{
        var tmp = thumbnail.replace("{width}", "100").replace("{height}", "100")
//                    var tmp =
//                        it.avatar?.path

        //println("avatar: $tmp")

        return if (tmp.isNullOrEmpty()) "" else Const.MEGAPLAN_URL + tmp
    }

}