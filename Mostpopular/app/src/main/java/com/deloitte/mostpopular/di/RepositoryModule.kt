package com.deloitte.mostpopular.di

import com.deloitte.mostpopular.data.local.UserDao
import com.deloitte.mostpopular.data.repository.UserRepositoryImpl
import com.deloitte.mostpopular.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module()
@InstallIn(SingletonComponent::class)
class RepositoryModule {


    @Provides
    fun provideUserRepository(
        userDao: UserDao
    ): UserRepository = UserRepositoryImpl(userDao)

}