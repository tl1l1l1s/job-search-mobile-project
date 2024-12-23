package com.appproject.project_jobsearch.data.network

import android.content.Context
import com.appproject.project_jobsearch.R
import com.appproject.project_jobsearch.data.UserDao
import com.appproject.project_jobsearch.data.UserDatabase
import com.appproject.project_jobsearch.data.UserDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JobsService(val context: Context) {
    private val service: IJobs

    private val userDB : UserDatabase by lazy {
        UserDatabase.getDatabase(context)
    }
    private val userDao : UserDao by lazy {
        userDB.userDao()
    }

    private val applicatonType = "application/json"
    private val key = context.applicationContext.resources.getString(R.string.SARAMIN_API_KEY)

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://oapi.saramin.co.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(IJobs::class.java)
    }

    suspend fun getJobList(
        user: UserDto
    ) : Jobs {

        return service.getJobList(
            accessKey = key,
            stock = user.stock,
            sr = user.sr,
            locCd = user.locCd,
            jobMidCd = "2",
            jobCd = user.jobCd,
            jobType = user.jobType,
            eduLv = user.eduLv?.toString(),
            fields = "expiration-date",
            count = "50",
            acceptType = applicatonType
        ).jobs}

    suspend fun getJobDetail(id: String) : Job {
        return service.getJobDetail(key, id, "expiration-date", applicatonType).jobs.job[0]
    }
}