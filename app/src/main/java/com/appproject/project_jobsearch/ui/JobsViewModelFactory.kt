package com.appproject.project_jobsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appproject.project_jobsearch.data.JobsRepository

class JobsViewModelFactory(private val repo: JobsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(JobsViewModel::class.java)) {
            return JobsViewModel(repo) as T
        }
        return IllegalArgumentException("Unknown Viewmodel class") as T
    }
}