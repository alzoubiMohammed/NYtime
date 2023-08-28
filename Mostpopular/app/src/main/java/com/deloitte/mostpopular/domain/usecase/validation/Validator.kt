package com.deloitte.mostpopular.domain.usecase.validation

import javax.inject.Inject

data class Validator @Inject constructor(
     val validateEmail: ValidateEmail,
     val validateBirthDate: ValidateBirthDate,
     val validatePassword: ValidatePassword,
     val validateNationalId: ValidateNationalId,
     val validateFullName: ValidateFullName,
     val validatePhoneNumber: ValidatePhoneNumber,
)
