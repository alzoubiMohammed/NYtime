package com.deloitte.mostpopular.di

import com.deloitte.mostpopular.data.datasource.MostPopularRemoteDataSource
import com.deloitte.mostpopular.data.local.UserDao
import com.deloitte.mostpopular.data.repository.MostPopularRepositoryImpl
import com.deloitte.mostpopular.data.repository.UserRepositoryImpl
import com.deloitte.mostpopular.domain.repository.MostPopularRepository
import com.deloitte.mostpopular.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [RemoteDataSourceModule::class])
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideNewsRepository(
        mostPopularRemoteDataSource: MostPopularRemoteDataSource
    ): MostPopularRepository = MostPopularRepositoryImpl(mostPopularRemoteDataSource)

    @Provides
    fun provideUserRepository(
        userDao: UserDao
    ): UserRepository = UserRepositoryImpl(userDao)

}