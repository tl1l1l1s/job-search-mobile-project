package com.appproject.project_jobsearch

import android.app.Application
import com.appproject.project_jobsearch.data.UserDatabase
import com.appproject.project_jobsearch.data.UserRepository

class UserApplication : Application() {
    val userDatabase by lazy {
        UserDatabase.getDatabase(this)
    }

    val userRepo by lazy {
        UserRepository(userDatabase.userDao())
    }
}