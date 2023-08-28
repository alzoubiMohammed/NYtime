package com.deloitte.mostpopular.domain.usecase.validation

import com.deloitte.mostpopular.R
import javax.inject.Inject

class ValidatePhoneNumber @Inject constructor() {

    fun execute(phoneNumber: String): ValidationResult {
        if(phoneNumber.length < 10) {
            return ValidationResult(
                successful = false,
                errorMessageRes =R.string.the_phone_number_needs_to_consist_of_at_least_10_numbers
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}