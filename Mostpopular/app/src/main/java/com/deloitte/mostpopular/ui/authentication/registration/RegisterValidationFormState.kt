package com.deloitte.mostpopular.ui.authentication.registration

data class RegisterValidationFormState(
    val fullName: String = "",
    val fullNameError: Int? = null,
    val email: String = "",
    val emailError: Int? = null,
    val nationalId: String = "",
    val nationalIdError: Int? = null,
    val phoneNumber: String = "",
    val phoneNumberError: Int? = null,
    val dateOfBirth: String = "",
    val dateOfBirthError: Int? = null,
    val password: String = "",
    val passwordError: Int? = null
)

