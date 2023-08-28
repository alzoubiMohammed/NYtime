package com.deloitte.mostpopular.domain.repository

import com.deloitte.mostpopular.data.model.User
import com.deloitte.mostpopular.data.network.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun registerUser(user: User): Flow<Resource<User>>
    fun login(email: String, password: String): Flow<Resource<User>>
    fun isLoggedIn(): Flow<Resource<User?>>
    fun getUserInfo(): Flow<Resource<User>>
    fun logout(): Flow<Resource<Boolean>>
}