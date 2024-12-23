package com.appproject.project_jobsearch.data.network

import com.google.gson.annotations.SerializedName

data class Root (
    val jobs: Jobs
)

data class Jobs (
    val count : Long?,
    val total: String,
    var job : List<Job>,
)

data class Job (
    val url : String?,
    val active: Long?,
    var company : Company?,
    val position : Position?,
    val keyword : String?,
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
    @SerializedName("job-code")
    val jobCode: JobCode?,
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

data class JobCode (
    @SerializedName("name")
    val jobCdName : String?,
)

data class Salary (
    @SerializedName("name")
    val salaryName : String?,
)

data class CloseType (
    val code : String?,
    @SerializedName("name")
    val closeTypeName : String?
)