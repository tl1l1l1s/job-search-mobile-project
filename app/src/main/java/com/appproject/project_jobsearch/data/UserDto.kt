package com.appproject.project_jobsearch.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserDto (
    @PrimaryKey (autoGenerate = true)
    val _id : Int,
    var nickname : String?,
    var stock : String?,
    var jobType : String?,
    var locCd : String?,
    var jobCd : String?,
    var eduLv : Int?,
    var isDirecthire : Boolean?,
)