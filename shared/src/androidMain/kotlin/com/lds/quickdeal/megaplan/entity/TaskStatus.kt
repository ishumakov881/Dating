package com.lds.quickdeal.megaplan.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable(with = TaskStatusSerializer::class)
public enum class TaskStatus {


    @SerialName("reached_server")
    REACHED_SERVER, //"Отправлена на сервер"

    @SerialName("reached_mega_plan")
    REACHED_MEGA_PLAN, //Дошла в мегаплан

    /**
     *
     * Статусы мегаплана
     *
     */

    @SerialName("created")
    CREATED,

    @SerialName("assigned")
    ASSIGNED,

    @SerialName("accepted")
    ACCEPTED,

    @SerialName("done")
    DONE,

    @SerialName("completed")
    COMPLETED,//Завершена

    @SerialName("rejected")
    REJECTED,

    @SerialName("cancelled")
    CANCELLED,

    @SerialName("expired")
    EXPIRED,

    @SerialName("delayed")
    DELAYED,

    @SerialName("template")
    TEMPLATE,

    @SerialName("overdue")
    OVERDUE,


    @SerialName("none")
    NONE
}

