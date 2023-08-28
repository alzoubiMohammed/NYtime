package com.deloitte.mostpopular.data.model

data class User(
    val fullName: String,
    val email: String,
    val nationalId: String,
    val phoneNumber: String,
    val dateOfBirth: String,
    val password: String
)