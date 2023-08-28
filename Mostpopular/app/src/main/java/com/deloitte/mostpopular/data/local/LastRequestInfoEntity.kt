package com.deloitte.mostpopular.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deloitte.mostpopular.data.local.AppDatabase.Companion.LAST_REQUEST_INFO_TABLE_NAME

@Entity(tableName = LAST_REQUEST_INFO_TABLE_NAME)
data class LastRequestInfoEntity(
    @PrimaryKey() val id: Int = 1, // Using a single row with a fixed ID
    val lastUpdateTimestamp: Long?,
    val period: String?
)