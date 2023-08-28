package com.deloitte.mostpopular.domain.usecase.validation


import com.deloitte.mostpopular.R
import javax.inject.Inject

class ValidateFullName @Inject constructor() {

    fun execute(fullName: String): ValidationResult {
        if(fullName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessageRes = R.string.the_fullname_can_t_be_blank
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}