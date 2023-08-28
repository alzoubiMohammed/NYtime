package com.deloitte.mostpopular.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.deloitte.mostpopular.data.local.AppDatabase.Companion.USER_TABLE_NAME

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
     fun insertUser(userEntity: UserEntity)

    @Query("SELECT * FROM $USER_TABLE_NAME WHERE email=:email")
     fun getUserByEmail(email: String): UserEntity?

    @Query("UPDATE $USER_TABLE_NAME SET isLoggedIn=:isLoggedIn WHERE email=:email")
     fun updateLoginStatus(email: String, isLoggedIn: Boolean)

    @Query("SELECT * FROM $USER_TABLE_NAME WHERE isLoggedIn = 1")
     fun getLoggedInUser(): UserEntity?

    @Query("SELECT * FROM $USER_TABLE_NAME WHERE isLoggedIn = 1")
     fun getUserLoggedInInfo(): UserEntity

    @Query("UPDATE $USER_TABLE_NAME SET isLoggedIn=0 WHERE isLoggedIn=1")
    fun logout()

}