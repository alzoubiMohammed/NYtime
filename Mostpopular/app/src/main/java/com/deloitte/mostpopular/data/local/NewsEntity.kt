package com.deloitte.mostpopular.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deloitte.mostpopular.data.local.AppDatabase.Companion.NEWS_TABLE_NAME

@Entity(NEWS_TABLE_NAME)
data class NewsEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "period") val period: String)

