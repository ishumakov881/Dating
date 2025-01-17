package com.lds.desktop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

    //val subject: String? = null

    @SerializedName("name")
    @Expose
    val name: String? = null //    @SerializedName("isOverdue")

    //    @Expose
    //    private Boolean isOverdue;
    //    @SerializedName("status")
    //    @Expose
    //    private String status;
    @SerializedName("owner")
    @Expose
    val owner: Owner? = null

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

@Serializable
data class Owner(
    @SerialName("contentType")
    val contentType: String = "Employee", // Всегда "Employee"
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String = "",
//    @SerialName("firstName")
//    val firstName: String = "",
//    @SerialName("middleName")
//    val middleName: String? = null,
//    @SerialName("lastName")
//    val lastName: String = "",
//    @SerialName("position")
//    val position: String = "",
//    @SerialName("department")
//    val department: Department? = null
) {
//    // Дополнительный конструктор на 2 параметра
//    constructor(contentType_: String, id: String) : this(
//        contentType = contentType_,
//        id = id
//    )
}