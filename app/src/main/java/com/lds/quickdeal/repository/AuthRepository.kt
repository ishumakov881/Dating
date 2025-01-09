package com.lds.quickdeal.repository

interface AuthRepository {
    suspend fun login(username: String, password: String) : Result<String>
}