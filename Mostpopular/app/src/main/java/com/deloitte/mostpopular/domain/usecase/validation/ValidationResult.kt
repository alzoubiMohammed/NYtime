package com.deloitte.mostpopular.domain.usecase.validation

data class ValidationResult(
    val successful: Boolean,
    val errorMessageRes: Int? = null
)