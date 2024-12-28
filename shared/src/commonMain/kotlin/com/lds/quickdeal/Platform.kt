package com.lds.quickdeal

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform