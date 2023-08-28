package com.deloitte.mostpopular.domain.usecase.validation

import com.deloitte.mostpopular.R
import javax.inject.Inject

class ValidatePassword @Inject constructor() {

    fun execute(password: String): ValidationResult {
        if(password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessageRes = R.string.the_password_needs_to_consist_of_at_least_8_characters
            )
        }
        val containsLettersAndDigits = password.any { it.isDigit() } &&
                password.any { it.isLetter() }
        if(!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessageRes =R.string.the_password_needs_to_contain_at_least_one_letter_and_digit
            )
        }
        val containsSmallAndBigLetters = password.any { it.isLowerCase() } &&
                password.any { it.isUpperCase() }
        if(!containsSmallAndBigLetters) {
            return ValidationResult(
                successful = false,
                errorMessageRes = R.string.the_password_needs_to_contain_at_least_one_small_letter_and_big
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}