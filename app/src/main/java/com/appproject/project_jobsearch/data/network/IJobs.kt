package com.appproject.project_jobsearch.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface IJobs {

    @GET("job-search")
    suspend fun getJobList(
        @Query("access-key") accessKey: String,
        @Query("stock") stock: String?,
        @Query("sr") sr: String?,
        @Query("loc_cd") locCd: String?,
        @Query("job_mid_cd") jobMidCd: String?,
        @Query("job_cd") jobCd: String?,
        @Query("job_type") jobType: String?,
        @Query("edu_lv") eduLv: String?,
        @Query("fields") fields: String,
        @Query("count") count: String,
        @Header("Accept") acceptType : String,
    ) : Root

    @GET("job-search")
    suspend fun getJobDetail(
        @Query("access-key") accessKey: String,
        @Query("id") id: String,
        @Query("fields") fields: String,
        @Header("Accept") acceptType : String,
    ) : Root
}