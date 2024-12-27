package com.lds.quickdeal.megaplan.entity

import com.lds.quickdeal.android.entity.TaskStatus
import com.lds.quickdeal.megaplan.response.AttachesInfo
import com.lds.quickdeal.megaplan.response.Bonus
import com.lds.quickdeal.megaplan.response.Comment
import com.lds.quickdeal.megaplan.response.DateInterval
import com.lds.quickdeal.megaplan.response.DateTime
import com.lds.quickdeal.megaplan.response.Employee
import com.lds.quickdeal.megaplan.response.FinOperation
import com.lds.quickdeal.megaplan.response.NegotiationItem
import com.lds.quickdeal.megaplan.response.ParentEntity
import com.lds.quickdeal.megaplan.response.Project
import com.lds.quickdeal.megaplan.response.Reminder
import com.lds.quickdeal.megaplan.response.Schedule
import com.lds.quickdeal.megaplan.response.Tag
import com.lds.quickdeal.megaplan.response.Task
import com.lds.quickdeal.megaplan.response.TaskRights
import com.lds.quickdeal.megaplan.response.Todo
import com.lds.quickdeal.megaplan.response.UserCreated
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


// Server ...

@Serializable
data class TaskResponse(


    @SerialName("synced")
    val synced: String? = null,

    @SerialName("megaplanId")
    val megaplanId: String = "",

    val name: String,
    val subject: String,

    val isTemplate: Boolean? = null,
    val isUrgent: Boolean? = null,

    val originalTemplate: Task? = null,

    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null,

//    @SerialName("timeCreated") val createdAt: String? = null,
//    @SerialName("statusChangeTime") val updatedAt: String? = null,


//    val username: String,
//    @SerialName("app_version") val appVersion: String,
//    val localId: String,

//    val emailsCount: Int = 0,
//    val whatsappCount: Int = 0,
//    val telegramCount: Int = 0,
//    val instagramCount: Int = 0,
//
//    val editableFields: List<String>? = null,
//
//    //Original items from megaplan
//    val contentType: String, // Всегда Task

//    val isNegotiation: Boolean? = null,
//    val negotiationItems: List<NegotiationItem>? = null,
//    val negotiationItemsCount: Int? = null,
//    val status: String,
//    val previousTasks: List<Task>? = null,
//    val previousTasksCount: Int? = null,
//    val nextTasksCount: Int? = null,
//    val parents: List<ParentEntity>? = null,
//    val project: Project? = null,
//    val rights: TaskRights? = null,
//    val possibleActions: List<String>? = null,
//    val parent: ParentEntity? = null,
//    val subTasks: List<Task>? = null,
//    val subTasksCount: Int? = null,
//    val actualSubTasksCount: Int? = null,
//    val bonuses: List<Bonus>? = null,
//    val fine: List<Bonus>? = null,
//    val fineCount: Int? = null,
//    val bonusesCount: Int? = null,
//    val isGroup: Boolean? = null,
//    val schedule: Schedule? = null,
//    val workedOffTime: List<Comment>? = null,
//    val workedOffTimeCount: Int? = null,
//    val workedOffTimeTotal: DateInterval? = null,
//    val userCreated: UserCreated? = null,
//    val finalRating: Int? = null,
//    val messagesCount: Int? = null,
//    val humanNumber: Int? = null,
//
//
//    val templateUsersCount: Int? = null,
//    val isTemplateOwnerCurrentUser: Boolean? = null,
//    val owner: Employee,
//    val responsible: Responsible,
//
//
//    val deadlineReminders: List<Reminder>? = null,
//    val deadlineRemindersCount: Int? = null,
//    val isOverdue: Boolean? = null,
//    val activity: String? = null,

//@@@    val templateUsers: /*List<User>*/String? = null,
//@@@    val deadline: String? = null,
//@@@    val auditors: List<User>? = null,
//@@@    val auditorsCount: Int? = null,
//@@@    val executors: /*List<User>*/String? = null,
//@@@    val executorsCount: Int? = null,
//@@@    val participants: /*List<User>*/String? = null,
//    val participantsCount: Int? = null,
//    val completed: Int? = null,
//    val attaches: String? = null,
//    val attachesCount: Int? = null,
//    val statement: String? = null,
//    val textStatement: String? = null,
//    val actualFinish: DateTime? = null,
//    val plannedFinish: DateTime? = null,
//    val duration: DateInterval? = null,
//    val responsibleCanEditExtFields: Boolean? = null,
//    val executorsCanEditExtFields: Boolean? = null,
//    val auditorsCanEditExtFields: Boolean? = null,
//    val actualWork: DateInterval? = null,

//    val relationLinksCount: Int? = null,
//    val relationLinks: /*List<RelationLink>*/String? = null,
//    val links: /*List<RelationLink>*/String? = null,
//    val linksCount: Int? = null,
//    val allFiles: String? = null, // Может быть File или FileGroup
//    val allFilesCount: Int? = null,
//    val milestones: String? = null,
//    val milestonesCount: Int? = null,
//    val deals: String? = null,
//    val dealsCount: Int? = null,
//    val actualDealsCount: Int? = null,
//    val plannedWork: DateInterval? = null,
//    val deadlineChangeRequest: String? = null,
//    val contractor: String? = null,
//    val timeCreated: DateTime? = null,
//    val actualStart: DateTime? = null,
//    val parentsCount: Int? = null,
//    val statusChangeTime: DateTime? = null,
//    val entitiesByTemplate: List<Task>? = null,
//    val entitiesByTemplateCount: Int? = null,
//    val actualEntitiesByTemplateCount: Int? = null,
//    val isFavorite: Boolean? = null,
//    val lastComment: Comment? = null,
//    val lastCommentTimeCreated: DateTime? = null,
//    val id: String,
//    val unreadCommentsCount: Int? = null,
//    val attachesCountInComments: Int? = null,
//    val subscribed: Boolean? = null,
//    val possibleSubscribers: /*List<User>*/String? = null,
//    val possibleSubscribersCount: Int? = null,
//    val subscribers: String? = null,
//    val subscribersCount: Int? = null,
//    val comments: List<Comment>? = null,
//    val commentsCount: Int? = null,
//    val hiddenCommentsCount: Int? = null,
//    val isUnread: Boolean? = null,
//    val firstUnreadComment: Comment? = null,
//    val unreadAnswer: Boolean? = null,
//    val lastView: DateTime? = null,
//    val tags: List<Tag>? = null,
//    val tagsCount: Int? = null,
//    val reminderTime: DateTime? = null,
//    val financeOperations: List<FinOperation>? = null,
//    val financeOperationsCount: Int? = null,
//    val attachesInfo: AttachesInfo? = null,
//    val todos: List<Todo>? = null,
//    val todosCount: Int? = null,
//    val actualTodosCount: Int? = null,
//    val finishedTodosCount: Int? = null
) {



    fun getStatus(): TaskStatus {

        //assigned  "status": "assigned",

        var completed = !this.synced.isNullOrEmpty() && !this.megaplanId.isNullOrEmpty()
        println("COMPLETED ?? $completed")
        //@@@@
        //return TaskStatus.COMPLETED
        return TaskStatus.COMPLETED
    }
}

@Serializable
data class Attach(
    val name: String,
    val type: String,
    val url: String
)


//data class TaskResponse(
//    val meta: Meta,
//    val data: List<FileData>
//)
//
//data class Meta(
//    val status: Int,
//    val errors: List<String>
//)
//
//data class FileData(
//    val contentType: String,
//    val id: String,
//    val name: String,
//    val mimeType: String,
//    val extension: String,
//    val size: Int,
//    val timeCreated: String,
//    val path: String,
//    val possibleActions: List<String>,
//    val metadata: Any? // Если структура metadata станет известна, замените Any на конкретный тип
//)