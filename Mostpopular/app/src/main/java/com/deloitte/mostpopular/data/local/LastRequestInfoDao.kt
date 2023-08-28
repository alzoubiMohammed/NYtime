package com.deloitte.mostpopular.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.deloitte.mostpopular.data.local.AppDatabase.Companion.LAST_REQUEST_INFO_TABLE_NAME

@Dao
interface LastRequestInfoDao {
    @Query("SELECT lastUpdateTimestamp, period,id  FROM $LAST_REQUEST_INFO_TABLE_NAME WHERE id = 1")
     fun getLastRequestInfo(): LastRequestInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun updateLasRequestInfo(timestampEntity: LastRequestInfoEntity)
}