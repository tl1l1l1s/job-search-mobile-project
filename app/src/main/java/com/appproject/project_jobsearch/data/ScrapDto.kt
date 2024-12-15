package com.appproject.project_jobsearch.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "scrap_table")
data class ScrapDto (
    @PrimaryKey(autoGenerate = true)
    val _id : Int,
    var employmentId : String,
)