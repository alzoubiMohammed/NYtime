package com.deloitte.mostpopular.di

import com.deloitte.mostpopular.data.datasource.MostPopularRemoteDataSource
import com.deloitte.mostpopular.data.local.NewsDao
import com.deloitte.mostpopular.data.local.LastRequestInfoDao
import com.deloitte.mostpopular.data.network.MostPopularEndPoint
import com.deloitte.mostpopular.domain.datasource.MostPopularRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [EndpointModule::class,LocalModule::class])
@InstallIn(SingletonComponent::class)
class RemoteDataSourceModule {

    @Provides
    @Singleton
    fun provideNewsRemoteDataSource(
        mostPopularEndPoint: MostPopularEndPoint,
        mostViewedDao: NewsDao,
        updateLastRequestInfoDao: LastRequestInfoDao,
    ): MostPopularRemoteDataSource =
        MostPopularRemoteDataSourceImpl(mostPopularEndPoint,mostViewedDao,updateLastRequestInfoDao)

}