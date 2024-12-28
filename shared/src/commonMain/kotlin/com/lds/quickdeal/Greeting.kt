package com.lds.quickdeal

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello..., ${platform.name}!"
    }
}