package com.lds.desktop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TaskResponse {
    @SerializedName("meta")
    @Expose
    var meta: Meta? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null
}