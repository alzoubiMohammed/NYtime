package com.deloitte.mostpopular.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.deloitte.mostpopular.data.local.AppDatabase.Companion.NEWS_TABLE_NAME

@Dao
interface NewsDao {

    @Query("SELECT * FROM $NEWS_TABLE_NAME WHERE period=:period")
    fun getAllItems(period:String): List<NewsEntity>

    @Query("SELECT * FROM $NEWS_TABLE_NAME WHERE title LIKE :title || '%' AND period=:period")
    fun search(title: String,period: String): List<NewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(items: List<NewsEntity>)
}