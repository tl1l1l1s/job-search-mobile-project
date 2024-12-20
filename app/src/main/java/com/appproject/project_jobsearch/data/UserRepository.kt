package com.appproject.project_jobsearch.data

class UserRepository(private val userDao : UserDao) {
    val currentUser : UserDto = userDao.getUser()

    suspend fun newUser(user: UserDto) {
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: UserDto) {
        userDao.updateUser(user)
    }
}