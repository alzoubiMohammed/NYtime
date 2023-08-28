package com.deloitte.mostpopular.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.deloitte.mostpopular.data.local.UserDao
import com.deloitte.mostpopular.data.local.UserEntity
import com.deloitte.mostpopular.data.model.User
import com.deloitte.mostpopular.data.network.Resource
import com.deloitte.mostpopular.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {

    override fun registerUser(user: User): Flow<Resource<User>> {
        return flow {
            try {
                delay(2000) // Simulate network call
                userDao.insertUser(user.toUserEntity())
                emit(Resource.Success(user))
            } catch (e: SQLiteConstraintException) {
                emit(Resource.Error(e))
            }
        }.onStart {
            emit(Resource.Loading)
        }.flowOn(IO)


    }

    override fun login(email: String, password: String): Flow<Resource<User>> {
        return flow {
            delay(2000) // Simulate network call
            val userEntity = userDao.getUserByEmail(email)
            if (userEntity != null && userEntity.password == password) {
                userDao.updateLoginStatus(email, true)
                emit(Resource.Success(userEntity.toUser()))
            } else {
                emit(Resource.Error(WrongEmailOrPasswordException()))
            }
        }.onStart {
            emit(Resource.Loading)
        }.flowOn(IO)


    }

    override fun isLoggedIn(): Flow<Resource<User?>> {
        return flow {
            try {
                delay(2000) // Simulate network call
                val userEntity = userDao.getLoggedInUser()
                emit(Resource.Success(userEntity?.toUser()))
            } catch (e: Exception) {
                emit(Resource.Error(e))

            }

        }.onStart {
            emit(Resource.Loading)
        }.flowOn(IO)

    }

    override fun getUserInfo(): Flow<Resource<User>> {
        return flow {
            try {
                delay(2000) // Simulate network call
                val userEntity = userDao.getUserLoggedInInfo()
                emit(Resource.Success(userEntity.toUser()))
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }

        }.onStart {
            emit(Resource.Loading)
        }.flowOn(IO)
    }

    override fun logout(): Flow<Resource<Boolean>> {
        return flow {
            try {
                delay(2000) // Simulate network call
                 userDao.logout()
                emit(Resource.Success(true))
            } catch (e: Exception) {
                emit(Resource.Error(e))
            }

        }.onStart {
            emit(Resource.Loading)
        }.flowOn(IO)
    }


    private fun UserEntity.toUser(): User {
        return User(
            fullName,
            email,
            nationalId,
            phoneNumber,
            dateOfBirth,
            password
        )

    }

    private fun User.toUserEntity(): UserEntity {
        return UserEntity(
            email,
            fullName,
            nationalId,
            phoneNumber,
            dateOfBirth,
            password,
            true
        )

    }

    class WrongEmailOrPasswordException :
        RuntimeException("email or password you have entered is not correct")


}