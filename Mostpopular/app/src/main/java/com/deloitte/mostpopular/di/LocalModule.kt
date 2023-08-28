package com.deloitte.mostpopular.di

import com.deloitte.mostpopular.data.local.AppDatabase
import com.deloitte.mostpopular.data.local.NewsDao
import com.deloitte.mostpopular.data.local.LastRequestInfoDao
import com.deloitte.mostpopular.data.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [AppModule::class])
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Singleton
    @Provides
    fun provideMostViewedDao(database: AppDatabase): NewsDao {
     return   database.mostViewedDao()
    }
    @Singleton
    @Provides
    fun provideLastRequestInfoDao(database: AppDatabase): LastRequestInfoDao {
     return   database.updateLastRequestInfoDao()
    }
    @Singleton
    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
     return   database.userDao()
    }


}