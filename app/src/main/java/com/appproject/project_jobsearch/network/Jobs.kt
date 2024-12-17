package com.appproject.project_jobsearch.network

import com.google.gson.annotations.SerializedName

data class Jobs (
    val count : String?,
    val jobs : List<Job>,
)

data class Job (
    val url : String?,
    val company : Company?,
    val position : Position?,
    val salary: Salary?,
    val id : String?,
    @SerializedName("expiration-date")
    val expiration : String?,
    @SerializedName("close-type")
    val closeType : CloseType?,
)

data class Company (
    @SerializedName("detail")
    val companyDetail : CompanyDetail?,
)

data class CompanyDetail (
    @SerializedName("name")
    val companyName : String?,
)

data class Position (
    val title : String?,
    val industry: Industry?,
    val location: Location?,
    @SerializedName("job-type")
    val jobType : JobType?,
)

data class Industry (
    @SerializedName("name")
    val industryName : String?,
)

data class Location (
    @SerializedName("name")
    val locationName : String?,
)

data class JobType (
    @SerializedName("name")
    val jobTypeName : String?,
)

data class Salary (
    val salaryName : String?,
)

data class CloseType (
    val code : String?,
)