package com.deloitte.mostpopular.domain.usecase

import com.deloitte.mostpopular.data.model.User
import com.deloitte.mostpopular.data.network.Resource
import com.deloitte.mostpopular.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val userRepository: UserRepository) {
    fun execute(email:String,password:String): Flow<Resource<User>> {
       return userRepository.login(email,password)
    }
}