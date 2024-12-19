package com.lds.quickdeal.android.entity

enum class TaskStatus {

    NONE,

    REACHED_SERVER,//Дошла на сервак
    REACHED_MEGA_PLAN,//Дошла в мегаплан
    COMPLETED//Выполнена, то есть закрыта кем-то
}