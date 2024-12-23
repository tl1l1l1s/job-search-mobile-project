package com.appproject.project_jobsearch.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity (tableName = "scrap_table")
data class ScrapDto (
    @PrimaryKey(autoGenerate = true)
    val _id : Int,
    var employmentId : String,
    var companyName : String?,
    var jobCdName : String?,
    var closeTypeCode: String?,
    var closeTypeName: String?,
    var expirationDate : String?,
)