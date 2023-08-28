package com.deloitte.mostpopular.domain.usecase.validation

import android.util.Patterns
import com.deloitte.mostpopular.R
import javax.inject.Inject

class ValidateEmail @Inject constructor() {

    fun execute(email: String): ValidationResult {
        if(email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessageRes = R.string.the_email_can_t_be_blank
            )
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessageRes =R.string.that_s_not_a_valid_email
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}