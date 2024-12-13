package com.lds.quickdeal.network

import kotlinx.serialization.Serializable


@Serializable
data class AuthResponse(
    val access_token: String,
    val expires_in: Long,
    val token_type: String,
    val scope: String? = null,
    val refresh_token: String
)