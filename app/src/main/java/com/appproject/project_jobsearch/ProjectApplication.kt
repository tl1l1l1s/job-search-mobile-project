package com.appproject.project_jobsearch

import android.app.Application
import com.appproject.project_jobsearch.data.JobsRepository
import com.appproject.project_jobsearch.data.ScrapDatabase
import com.appproject.project_jobsearch.data.UserDatabase
import com.appproject.project_jobsearch.data.UserRepository
import com.appproject.project_jobsearch.data.network.JobsService

class ProjectApplication : Application() {
    private val userDatabase by lazy {
        UserDatabase.getDatabase(this)
    }

    val userRepo by lazy {
        UserRepository(userDatabase.userDao())
    }

    private val jobsService by lazy {
        JobsService(this)
    }

    val jobsRepo by lazy {
        JobsRepository(jobsService)
    }

    val scrapDatabase by lazy {
        ScrapDatabase.getDatabase(this)
    }

    //TODO scrapRepository
}