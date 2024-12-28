package com.lds.quickdeal.megaplan.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Owner(
    @SerialName("contentType")
    val contentType: String = "Employee", // Всегда "Employee"
    @SerialName("id")
    val id: String
//    @SerialName("name")
//    val name: String = "",
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