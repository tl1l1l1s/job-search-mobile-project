package com.appproject.project_jobsearch.data

import com.appproject.project_jobsearch.data.network.Job
import com.appproject.project_jobsearch.data.network.Jobs
import com.appproject.project_jobsearch.data.network.JobsService

class JobsRepository(private val jobsService: JobsService) {

    suspend fun getList(user: UserDto) : Jobs {
        return jobsService.getJobList(user)
    }

    suspend fun getDetail(id: String) : Job {
        return jobsService.getJobDetail(id)
    }

}