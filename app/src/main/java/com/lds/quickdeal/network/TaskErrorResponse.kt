package com.lds.quickdeal.network

data class TaskErrorResponse(
    val statusCode: Int,
    val message: String
)

//data class TaskErrorResponse(
//    val meta: Meta,
//    val data: Any? = null
//)
//
//data class Meta(
//    val status: Int,
//    val errors: List<ErrorDetail>
//)
//
//data class ErrorDetail(
//    val field: String?,
//    val type: String,
//    val message: String,
//    val trace: List<String>
//)