package com.lds.quickdeal.megaplan.entity


fun getPrettyStatus(synced: String?, megaplanId: String?, status: TaskStatus?): TaskStatus {

    //assigned  "status": "assigned",

    //"status":"accepted"
    //created, assigned, accepted, done, completed, rejected, cancelled, expired, delayed, template, overdue

    val reachedMegaplan = !synced.isNullOrEmpty() && !megaplanId.isNullOrEmpty()
    //var completed = ...

    println("REACHED_MEGA_PLAN ?? $reachedMegaplan")
    println("Status==> $status")
    //println("COMPLETED ?? $reachedMegaplan")

    return when {

        status != null -> {
            status
        }

        reachedMegaplan -> {
            TaskStatus.REACHED_MEGA_PLAN
        }

        else -> {
            TaskStatus.REACHED_SERVER
        }
    }

    //return TaskStatus.REACHED_SERVER

}