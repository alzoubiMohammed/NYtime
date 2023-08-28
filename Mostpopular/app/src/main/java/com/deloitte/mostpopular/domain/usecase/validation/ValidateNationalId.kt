package com.deloitte.mostpopular.domain.usecase.validation

import com.deloitte.mostpopular.R
import javax.inject.Inject

class ValidateNationalId @Inject constructor() {

    fun execute(nationalId: String): ValidationResult {
        if(nationalId.length < 10) {
            return ValidationResult(
                successful = false,
                errorMessageRes =R.string.the_national_id_needs_to_consist_of_at_least_10_numbers
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}