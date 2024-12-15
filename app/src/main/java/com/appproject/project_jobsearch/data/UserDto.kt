package com.appproject.project_jobsearch.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserDto (
    @PrimaryKey (autoGenerate = true)
    val _id : Int,
    var nickname : String?,
    @ColumnInfo(name = "companyType")
    var coTp : String?,
    var region : String?,
    var occupation : String?,
    var education : String?,
    var career : String?,
    var pref : Boolean?,
)