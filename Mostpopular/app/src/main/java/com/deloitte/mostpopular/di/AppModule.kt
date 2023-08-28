package com.deloitte.mostpopular.di

import android.content.Context
import androidx.room.Room
import com.deloitte.mostpopular.data.local.AppDatabase
import com.deloitte.mostpopular.data.local.AppDatabase.Companion.DATA_BASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context):AppDatabase{
     return Room.databaseBuilder(context,AppDatabase::class.java,DATA_BASE_NAME).build()
    }


}