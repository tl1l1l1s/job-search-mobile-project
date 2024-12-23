package com.appproject.project_jobsearch.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appproject.project_jobsearch.data.JobsRepository
import com.appproject.project_jobsearch.data.UserDto
import com.appproject.project_jobsearch.data.network.Job
import com.appproject.project_jobsearch.data.network.Jobs
import kotlinx.coroutines.launch

class JobsViewModel(private val jobsRepository: JobsRepository) : ViewModel() {

    private val _jobs = MutableLiveData<Jobs>()
    val jobs : LiveData<Jobs> = _jobs

    fun getList(user: UserDto) = viewModelScope.launch {
        _jobs.value = jobsRepository.getList(user)
    }

    private val _job = MutableLiveData<Job>()
    val job : LiveData<Job> = _job

    fun getDetail(id: String) = viewModelScope.launch {
        _job.value = jobsRepository.getDetail(id)
    }
}