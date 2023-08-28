package com.deloitte.mostpopular.di

import com.deloitte.mostpopular.data.network.MostPopularEndPoint
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [RetrofitModule::class])
@InstallIn(SingletonComponent::class)
class EndpointModule {

    @Singleton
    @Provides
    fun provideMostPopularEndPoint(retrofit: Retrofit) = retrofit.create(MostPopularEndPoint::class.java)


}