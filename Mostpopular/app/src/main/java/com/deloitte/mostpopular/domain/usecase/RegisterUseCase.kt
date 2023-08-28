package com.deloitte.mostpopular.domain.usecase

import com.deloitte.mostpopular.data.model.User
import com.deloitte.mostpopular.data.network.Resource
import com.deloitte.mostpopular.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val userRepository: UserRepository) {
    fun execute(user:User): Flow<Resource<User>> {
        return userRepository.registerUser(user)
    }
}