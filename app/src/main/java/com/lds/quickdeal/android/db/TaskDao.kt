package com.lds.quickdeal.android.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lds.quickdeal.android.entity.UploaderTask

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: UploaderTask)

    @Query("SELECT * FROM tasks ORDER BY _id DESC")
    suspend fun getAllTasks(): List<UploaderTask>

    @Delete
    suspend fun delete(task: UploaderTask)

    @Update
    suspend fun update(task: UploaderTask)

    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM tasks WHERE _id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: String): UploaderTask?

    @Query("UPDATE tasks SET name = :name, subject = :description WHERE _id = :taskId")
    suspend fun updateById(taskId: Long, name: String, description: String)


    @Query("UPDATE tasks SET name = :name, subject = :subject WHERE megaplanId = :megaplanId")
    suspend fun updateByMegaplanId(megaplanId: String, name: String, subject: String)

}
