package com.lds.desktop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Meta {
    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("errors")
    @Expose
    var errors: List<Any>? = null

    @SerializedName("pagination")
    @Expose
    var pagination: Pagination? = null
}