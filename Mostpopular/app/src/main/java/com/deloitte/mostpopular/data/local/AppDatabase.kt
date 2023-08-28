package com.deloitte.mostpopular.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsEntity::class,LastRequestInfoEntity::class,UserEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao():UserDao
    abstract fun mostViewedDao():NewsDao
    abstract fun updateLastRequestInfoDao():LastRequestInfoDao

    companion object{
        const val DATA_BASE_NAME="most_popular"
        const val USER_TABLE_NAME="user_table"
        const val NEWS_TABLE_NAME="news_table"
        const val LAST_REQUEST_INFO_TABLE_NAME="last_request_info_table"
    }
}