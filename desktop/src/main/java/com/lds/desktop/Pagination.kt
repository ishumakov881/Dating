package com.lds.desktop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Pagination {
    @SerializedName("count")
    @Expose
    var count: Int? = null

    @SerializedName("limit")
    @Expose
    var limit: Int? = null

    @SerializedName("currentPage")
    @Expose
    var currentPage: Int? = null

    @SerializedName("hasMoreNext")
    @Expose
    var hasMoreNext: Boolean? = null

    @SerializedName("hasMorePrev")
    @Expose
    var hasMorePrev: Boolean? = null
}