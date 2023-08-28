package com.deloitte.mostpopular.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao():UserDao


    companion object{
        const val DATA_BASE_NAME="most_popular"
        const val USER_TABLE_NAME="user_table"
    }
}