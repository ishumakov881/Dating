package com.lds.quickdeal.megaplan.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.lang.reflect.Constructor

@Serializable
data class TaskRequest(

    val name: String? = null, // Название
    val subject: String? = null, // Описание задачи
    val owner: Owner? = null, // Владелец (Создатель)
    val contentType: String = "Task", // всегда равен "Task"
    val responsible: Owner? = null, // Ответственный - UserUnion
    val parent: Parent? = null, // Надзадача/надпроект

    val isUrgent: Boolean? = null, // Горящая
//    val isNegotiation: Boolean? = null, // Согласование
//    val negotiationItems: List<NegotiationItem>? = null, // Массив согласований
//    val negotiationItemsCount: Int? = null, // Количество согласований
//    val status: String? = null, // Статус
//    val previousTasks: List<Task>? = null, // Предыдущие задачи
//    val previousTasksCount: Int? = null, // Количество предыдущих задач
//    val nextTasksCount: Int? = null, // Количество следующих задач
//    val parents: List<Parent>? = null, // Массив надзадач/надпроектов
//    val project: Project? = null, // Корневой проект
//    val rights: TaskRights? = null, // Список возможных действий
//    val possibleActions: List<String>? = null, // Список возможных действий (Устаревшее)
//    val subTasks: List<Task>? = null, // Список подзадач
//    val subTasksCount: Int? = null, // Число подзадач
//    val actualSubTasksCount: Int? = null, // Число актуальных подзадач
//    val bonuses: List<Bonus>? = null, // Массив бонусов
//    val fine: List<Bonus>? = null, // Массив штрафов
//    val fineCount: Int? = null, // Количество штрафов
//    val bonusesCount: Int? = null, // Количество бонусов
//    val isGroup: Boolean? = null, // Массовая задача
//    val schedule: Schedule? = null, // Событие
//    val workedOffTime: List<Comment>? = null, // Отработанное время
//    val workedOffTimeCount: Int? = null, // Количество элементов "Отработанное время"
//    val workedOffTimeTotal: DateInterval? = null, // Общее отработанное время
//    val userCreated: UserUnion? = null, // Создатель
//    val finalRating: Int? = null, // Финальный рейтинг
//    val messagesCount: Int? = null, // Количество сообщений
//    val humanNumber: Int? = null, // Человекочитаемый номер
//
//
//
    val isTemplate: Boolean? = null, // Является шаблоном
//    val originalTemplate: Task? = null, // Оригинальный шаблон
//    val templateUsers: List<UserUnion>? = null, // Пользователи шаблона
//    val templateUsersCount: Int? = null, // Количество пользователей шаблона
//    val isTemplateOwnerCurrentUser: Boolean? = null, // Владелец текущий пользователь
//
//
//    val deadline: DeadlineUnion? = null, // Дата дедлайна
//    val deadlineReminders: List<Reminder>? = null, // Напоминания о неизбежном дедлайне
//    val deadlineRemindersCount: Int? = null, // Количество напоминаний о дедлайне
//    val isOverdue: Boolean? = null, // Просрочена
//    val activity: DateTime? = null, // Дата активности
//    val auditors: List<UserUnion>? = null, // Аудиторы
//    val auditorsCount: Int? = null, // Количество аудиторов
//    val executors: List<UserUnion>? = null, // Соисполнители
//    val executorsCount: Int? = null, // Количество соисполнителей
//    val participants: List<UserUnion>? = null, // Участники
//    val participantsCount: Int? = null, // Количество участников
//    val completed: Int? = null, // Выполнено
//    val attaches: List<File>? = null, // Вложения
//    val attachesCount: Int? = null, // Количество вложений
//    val statement: String? = null, // Текст задачи
//    val textStatement: String? = null, // Человекочитаемый текст задачи
//    val actualFinish: DateTime? = null, // Дата финиша задачи (закрытия)
//    val plannedFinish: DateTime? = null, // Планируемое время окончания
//    val duration: DateInterval? = null, // Планируемая длительность задачи
//    val responsibleCanEditExtFields: Boolean? = null, // Ответственные могут редактировать расширенные поля
//    val executorsCanEditExtFields: Boolean? = null, // Соисполнители могут редактировать расширенные поля
//    val auditorsCanEditExtFields: Boolean? = null, // Аудиторы могут редактировать расширенные поля
//    val actualWork: DateInterval? = null, // Актуальное время в мс
//    val relationLinks: List<RelationLink>? = null, // Связанные ссылки
//    val relationLinksCount: Int? = null, // Количество связанных ссылок
//    val links: List<RelationLink>? = null, // Связанные ссылки
//    val linksCount: Int? = null, // Количество связанных ссылок
//    val allFiles: List<FileUnion>? = null, // Список вложений
//    val allFilesCount: Int? = null, // Количество вложений
//    val milestones: List<Milestone>? = null, // Вехи
//    val milestonesCount: Int? = null, // Количество вех
//    val deals: List<Deal>? = null, // Сделки
//    val dealsCount: Int? = null, // Количество сделок
//    val actualDealsCount: Int? = null, // Количество актуальных сделок
//    val plannedWork: DateInterval? = null, // Планируемое время в мс
//    val deadlineChangeRequest: DeadlineChangeRequest? = null, // Запрос на изменение дедлайна
//    val contractor: UserUnion? = null, // Заказчик
//    val timeCreated: DateTime? = null, // Дата создания задачи (только чтение)
//    val actualStart: DateTime? = null, // Дата старта задачи (создания или принятия)
//    val parentsCount: Int? = null, // Количество родительских сущностей
//    val statusChangeTime: DateTime? = null, // Время последнего изменения статуса
//    val entitiesByTemplate: List<Task>? = null, // Сущности по шаблону
//    val entitiesByTemplateCount: Int? = null, // Количество сущностей по шаблону
//    val actualEntitiesByTemplateCount: Int? = null, // Актуальное количество сущностей по шаблону
//    val isFavorite: Boolean? = null, // Избранный
//    val lastComment: Comment? = null, // Последний комментарий
//    val lastCommentTimeCreated: DateTime? = null, // Дата последнего комментария
//    val id: String? = null, // Идентификатор
//    val unreadCommentsCount: Int? = null, // Количество непрочитанных комментариев
//    val attachesCountInComments: Int? = null, // Количество вложений в комментариях
//    val subscribed: Boolean? = null, // Подписан ли текущий пользователь на комментарии по этой сущности
//    val possibleSubscribers: List<UserUnion>? = null, // Пользователи-участники сущности, которые могут получать уведомления по ней
//    val possibleSubscribersCount: Int? = null, // Количество возможных подписчиков
//    val subscribers: List<UserUnion>? = null, // Пользователи-участники, которые подписаны на уведомления по сущности
//    val subscribersCount: Int? = null, // Количество подписчиков
//    val comments: List<Comment>? = null, // Массив комментариев
//    val commentsCount: Int? = null, // Количество комментариев
//    val hiddenCommentsCount: Int? = null, // Количество скрытых комментариев
//    val isUnread: Boolean? = null, // Помечен как непрочитанный
//    val firstUnreadComment: Comment? = null, // Первый непрочитанный комментарий
//    val unreadAnswer: Boolean? = null, // Есть ли непрочитанный ответ или упоминание
//    val lastView: DateTime? = null, // Дата последнего просмотра
//    val tags: List<Tag>? = null, // Метки
//    val tagsCount: Int? = null, // Количество меток
//    val reminderTime: DateTime? = null, // Дата напоминания
//    val financeOperations: List<FinOperation>? = null, // Финансовые операции
//    val financeOperationsCount: Int? = null, // Количество финансовых операций
//    val attachesInfo: AttachesInfo? = null, // Информация о вложении
//    val todos: List<Todo>? = null, // Массив дел
//    val todosCount: Int? = null, // Количество дел
//    val actualTodosCount: Int? = null, // Количество актуальных дел
//    val finishedTodosCount: Int? = null // Количество завершенных дел
)

@Serializable
data class Parent(
    val contentType: String = "Task", // всегда Task, как в примере
    val id: String // Идентификатор родительской задачи или проекта
)


