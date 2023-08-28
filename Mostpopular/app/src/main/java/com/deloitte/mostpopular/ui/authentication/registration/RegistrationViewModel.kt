package com.deloitte.mostpopular.ui.authentication.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deloitte.mostpopular.data.model.User
import com.deloitte.mostpopular.data.network.Resource
import com.deloitte.mostpopular.domain.usecase.RegisterUseCase
import com.deloitte.mostpopular.domain.usecase.validation.Validator
import com.deloitte.mostpopular.ui.authentication.registration.RegistrationViewModel.RegistrationAction.*
import com.deloitte.mostpopular.ui.authentication.registration.RegistrationViewModel.RegistrationEvent.*
import com.deloitte.mostpopular.ui.authentication.registration.RegistrationViewModel.RegistrationState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val validator: Validator
) :
    ViewModel() {


    private var formState = RegisterValidationFormState()

    private val _uiState = MutableStateFlow<RegistrationState>(Idle)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<RegistrationEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun processUiAction(action: RegistrationAction) {
        when (action) {
            is EmailChanged -> formState = formState.copy(email = action.email)
            is PasswordChanged -> formState =
                formState.copy(password = action.password)

            is DateOfBirthChanged -> formState =
                formState.copy(dateOfBirth = action.dateOfBirth)

            is FullNameChanged -> formState =
                formState.copy(fullName = action.fullName)

            is NationalIdChanged -> formState =
                formState.copy(nationalId = action.nationalId)

            is PhoneNumberChanged -> formState =
                formState.copy(phoneNumber = action.phoneNumber)

            RegisterClicked -> validateUserInput()

        }
    }

    private fun validateUserInput() {
        viewModelScope.launch { _uiState.emit(Idle) }

        val emailResult = validator.validateEmail.execute(formState.email)
        val passwordResult = validator.validatePassword.execute(formState.password)
        val nationalIdResult = validator.validateNationalId.execute(formState.nationalId)
        val fullNameResult = validator.validateFullName.execute(formState.fullName)
        val birthDateResult = validator.validateBirthDate.execute(formState.dateOfBirth)
        val phoneNumberResult = validator.validatePhoneNumber.execute(formState.phoneNumber)

        val hasError = listOf(
            emailResult,
            passwordResult,
            nationalIdResult,
            fullNameResult,
            birthDateResult,
            phoneNumberResult
        ).any { !it.successful }

        if (hasError) {
            formState =
                formState.copy(
                    emailError = emailResult.errorMessageRes,
                    passwordError = passwordResult.errorMessageRes,
                    nationalIdError = nationalIdResult.errorMessageRes,
                    fullNameError = fullNameResult.errorMessageRes,
                    dateOfBirthError = birthDateResult.errorMessageRes,
                    phoneNumberError = phoneNumberResult.errorMessageRes
                )
            _uiState.value = OnValidationChange(formState)

            return

        }

        registerUser()

    }

    private fun registerUser() {
        viewModelScope.launch {
            registerUseCase.execute(formState.toUser()).onCompletion { _uiState.value = Idle }
                .collect {
                    when (it) {
                        is Resource.Error -> _uiEvent.send(ShowToast(it.throwable.message))
                        Resource.Loading -> _uiState.value = Loading
                        is Resource.Success -> _uiEvent.send(OpenHomeScreen)
                    }
                }
        }

    }

    sealed class RegistrationAction {
        data class FullNameChanged(val fullName: String) : RegistrationAction()
        data class EmailChanged(val email: String) : RegistrationAction()
        data class NationalIdChanged(val nationalId: String) : RegistrationAction()
        data class PhoneNumberChanged(val phoneNumber: String) : RegistrationAction()
        data class DateOfBirthChanged(val dateOfBirth: String) : RegistrationAction()
        data class PasswordChanged(val password: String) : RegistrationAction()
        data object RegisterClicked : RegistrationAction()

    }

    sealed class RegistrationState {
        data object Idle : RegistrationState()
        data object Loading : RegistrationState()
        data class OnValidationChange(val state: RegisterValidationFormState) : RegistrationState()

    }

    sealed class RegistrationEvent {
        data class ShowToast(val message: String?) : RegistrationEvent()
        data object OpenHomeScreen : RegistrationEvent()
    }

    private fun RegisterValidationFormState.toUser(): User {
        return User(
            fullName = fullName,
            email = email,
            nationalId = nationalId,
            phoneNumber = phoneNumber,
            dateOfBirth = dateOfBirth,
            password = password
        )
    }
}