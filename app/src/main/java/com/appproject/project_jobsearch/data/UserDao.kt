package com.appproject.project_jobsearch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    fun insertUser(user : UserDto)

    @Update
    fun updateUser(user: UserDto)

    @Query("SELECT * FROM user_table")
    fun getUser() : UserDto
}