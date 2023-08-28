package com.deloitte.mostpopular.ui.authentication.login

data class LoginValidationFormState(
    val email: String = "",
    val emailError: Int? = null,
    val password: String = "",
    val passwordError: Int? = null
)

