package com.deloitte.mostpopular.domain.usecase

import com.deloitte.mostpopular.data.network.Resource
import com.deloitte.mostpopular.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val userRepository: UserRepository) {
    fun execute(): Flow<Resource<Boolean>> {
        return userRepository.logout()
    }
}