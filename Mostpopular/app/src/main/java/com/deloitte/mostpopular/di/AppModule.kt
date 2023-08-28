package com.deloitte.mostpopular.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.deloitte.mostpopular.R
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
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
            .placeholderOf(null)
            .placeholder(R.drawable.ic_download_24)
            .error(R.drawable.ic_error_24)

    }

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context,
        requestOptions: RequestOptions
    ): RequestManager {
        return Glide.with(context)
            .setDefaultRequestOptions(requestOptions)

    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context):AppDatabase{
     return Room.databaseBuilder(context,AppDatabase::class.java,DATA_BASE_NAME).build()
    }


}