package com.lds.quickdeal.megaplan.response

import com.lds.quickdeal.megaplan.entity.Owner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskResponse(
    @SerialName("meta")
    val meta: Meta,
    @SerialName("data")
    val data: TaskData
)

@Serializable
data class Meta(
    @SerialName("status")
    val status: Int,
    @SerialName("errors")
    val errors: List<String> = emptyList(),
//    @SerialName("pagination")
//    val pagination: List<Any> = emptyList()
)

@Serializable
data class TaskData(
    @SerialName("contentType")
    val contentType: String,
    @SerialName("id")
    val id: String,
    @SerialName("humanNumber")
    val humanNumber: Int,
    @SerialName("name")
    val name: String,
    @SerialName("isOverdue")
    val isOverdue: Boolean,
    @SerialName("status")
    val status: String,
    @SerialName("statusChangeTime")
    val statusChangeTime: DateTime?,
    @SerialName("owner")
    val owner: Owner?,
    @SerialName("responsible")
    val responsible: Responsible?,
    @SerialName("participants")
    val participants: List<Participant> = emptyList(),
    @SerialName("statement")
    val statement: String,
    @SerialName("textStatement")
    val textStatement: String,
    @SerialName("activity")
    val activity: DateTime?,
    @SerialName("isFavorite")
    val isFavorite: Boolean
)


@Serializable
data class Department(
    @SerialName("contentType")
    val contentType: String,
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
)


@Serializable
data class Participant(
    @SerialName("contentType")
    val contentType: String,
    @SerialName("id")
    val id: String
)

//@Serializable
//data class TaskResponse(
//    val contentType: String, // Всегда Task
//    val isUrgent: Boolean? = null,
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
//    val name: String,
//    val subject: String,
//    val isTemplate: Boolean? = null,
//    val originalTemplate: Task? = null,
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
//
//    val templateUsers: /*List<User>*/String? = null,
//    val deadline: String? = null,
//    val auditors: /*List<User>*/String? = null,
//    val auditorsCount: Int? = null,
//    val executors: /*List<User>*/String? = null,
//    val executorsCount: Int? = null,
//    val participants: /*List<User>*/String? = null,
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
//
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
//)


@Serializable
data class NegotiationItem(
    val type: String, // Тип согласования
    val name: String
)

@Serializable
data class Task(
    val id: String,
    val name: String
)

@Serializable
data class ParentEntity(
    val type: String,
    val id: String
)

@Serializable
data class Project(
    val id: String,
    val name: String
)

@Serializable
data class TaskRights(
    val canEdit: Boolean,
    val canDelete: Boolean
)

@Serializable
data class Bonus(
    val id: String,
    val amount: Double
)

@Serializable
data class Schedule(
    val start: DateTime,
    val end: DateTime
)

@Serializable
data class Comment(
    val id: String,
    val text: String,
    val createdAt: DateTime
)

@Serializable
data class DateInterval(
    val start: DateTime,
    val end: DateTime
)

@Serializable
data class DateTime(
    val dateTime: String,

    @SerialName("contentType")
    val contentType: String,
    @SerialName("value")
    val value: String
)

@Serializable
data class Employee(
    val id: String,
    val name: String
)

@Serializable
data class Reminder(
    val message: String,
    val time: DateTime
)

@Serializable
data class Tag(
    val name: String
)

@Serializable
data class FinOperation(
    val amount: Double,
    val description: String
)

@Serializable
data class AttachesInfo(
    val type: String,
    val size: Long
)

@Serializable
data class Todo(
    val id: String,
    val taskId: String,
    val name: String
)


@Serializable
data class Responsible(

    val type: String, // Employee, ContractorCompany, etc.


    @SerialName("contentType")
    val contentType: String,
    @SerialName("id")
    val id: String
)

@Serializable
data class UserCreated(
    val type: String, // Employee, ContractorHuman, etc.
    val id: String
)