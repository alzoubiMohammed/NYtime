package com.deloitte.mostpopular.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deloitte.mostpopular.data.local.AppDatabase.Companion.USER_TABLE_NAME

@Entity(tableName = USER_TABLE_NAME)
data class UserEntity(
    @PrimaryKey val email: String,
    val fullName: String,
    val nationalId: String,
    val phoneNumber: String,
    val dateOfBirth: String,
    val password: String,
    val isLoggedIn: Boolean

)
