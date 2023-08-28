package com.deloitte.mostpopular.ui.authentication.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import com.deloitte.mostpopular.data.network.Resource
import com.deloitte.mostpopular.domain.usecase.LoginUseCase
import com.deloitte.mostpopular.domain.usecase.validation.Validator
import com.deloitte.mostpopular.ui.authentication.login.LoginViewModel.LoginAction.*
import com.deloitte.mostpopular.ui.authentication.login.LoginViewModel.LoginEvent.*
import com.deloitte.mostpopular.ui.authentication.login.LoginViewModel.LoginState.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validator: Validator,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private var formState = LoginValidationFormState()

    private val _uiState = MutableStateFlow<LoginState>(Idle)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<LoginEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun processUiAction(action: LoginAction) {
        when (action) {
            is EmailChanged -> {
                formState = formState.copy(email = action.email)
            }

            is PasswordChanged -> {
                formState = formState.copy(password = action.password)
            }

            is Login -> {
                validateUserInput()
            }

        }
    }

    private fun validateUserInput() {
        viewModelScope.launch { _uiState.emit(Idle) }
        val emailResult = validator.validateEmail.execute(formState.email)
        val passwordResult = validator.validatePassword.execute(formState.password)

        val hasError = listOf(emailResult, passwordResult).any { !it.successful }

        if (hasError) {
            formState =
                formState.copy(
                    emailError = emailResult.errorMessageRes,
                    passwordError = passwordResult.errorMessageRes
                )
            _uiState.value = OnValidationChange(formState)

            return

        }
        doLogin()
    }

    private fun doLogin() {
        viewModelScope.launch {
            loginUseCase.execute(formState.email, formState.password)
                .onCompletion { _uiState.value = Idle }
                .collect {
                    when (it) {
                        is Resource.Error -> _uiEvent.send(ShowToast(it.throwable.message))
                        Resource.Loading -> _uiState.value = Loading
                        is Resource.Success -> _uiEvent.send(OpenHomeScreen)
                    }
                }
        }
    }


    sealed class LoginAction {
        data object Login : LoginAction()
        data class EmailChanged(val email: String) : LoginAction()
        data class PasswordChanged(val password: String) : LoginAction()
    }

    sealed class LoginState {
        data object Idle : LoginState()
        data object Loading : LoginState()
        data class OnValidationChange(val state: LoginValidationFormState) : LoginState()

    }

    sealed class LoginEvent {
        data class ShowToast(val message: String?) : LoginEvent()
        data object OpenHomeScreen : LoginEvent()
    }


}