package com.lds.desktop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Datum {
    @SerializedName("contentType")
    @Expose
    private val contentType: String? = null

    @SerializedName("id")
    @Expose
    val id: String? = null

    @SerializedName("humanNumber")
    @Expose
    private val humanNumber: Int? = null

    @SerializedName("name")
    @Expose
    val name: String? = null //    @SerializedName("isOverdue")
    //    @Expose
    //    private Boolean isOverdue;
    //    @SerializedName("status")
    //    @Expose
    //    private String status;
    //    @SerializedName("owner")
    //    @Expose
    //    private Owner owner;
    //    @SerializedName("deadline")
    //    @Expose
    //    private Object deadline;
    //    @SerializedName("responsible")
    //    @Expose
    //    private Responsible responsible;
    //    @SerializedName("isUrgent")
    //    @Expose
    //    private Boolean isUrgent;
    //    @SerializedName("isNegotiation")
    //    @Expose
    //    private Boolean isNegotiation;
    //    @SerializedName("isTemplate")
    //    @Expose
    //    private Boolean isTemplate;
    //    @SerializedName("rights")
    //    @Expose
    //    private Rights rights;
    //    @SerializedName("unreadCommentsCount")
    //    @Expose
    //    private Integer unreadCommentsCount;
    //    @SerializedName("isFavorite")
    //    @Expose
    //    private Boolean isFavorite;
    //    @SerializedName("tags")
    //    @Expose
    //    private List<Object> tags;
    //    @SerializedName("tagsCount")
    //    @Expose
    //    private Integer tagsCount;
}
