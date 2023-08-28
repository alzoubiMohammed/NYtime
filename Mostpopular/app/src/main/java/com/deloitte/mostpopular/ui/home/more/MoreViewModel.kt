package com.deloitte.mostpopular.ui.home.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deloitte.mostpopular.data.model.User
import com.deloitte.mostpopular.data.network.Resource
import com.deloitte.mostpopular.domain.usecase.GetUserInfoUseCase
import com.deloitte.mostpopular.domain.usecase.LogoutUseCase
import com.deloitte.mostpopular.ui.home.more.MoreViewModel.MoreViewEvent.*
import com.deloitte.mostpopular.ui.home.more.MoreViewModel.MoreViewState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiEvent = Channel<MoreViewEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    private val _uiState = MutableStateFlow<MoreViewState>(Idle)
    val uiState = _uiState.asStateFlow()


    init {
        getUserInfo()
    }

    fun processUiAction(action: MoreViewAction) {
        when (action) {
            is MoreViewAction.ChangeLanguage -> onLanguageChange(action.currentLanguageTag)
            MoreViewAction.LogoutClicked -> logout()
        }
    }

    private fun getUserInfo() {

        viewModelScope.launch {
            getUserInfoUseCase.execute().collect {
                when (it) {
                    is Resource.Error -> _uiEvent.send(ShowToast(it.throwable.message))
                    Resource.Loading -> _uiState.emit(Loading)
                    is Resource.Success -> _uiState.emit(Success(it.data))
                }
            }
        }

    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase.execute().collect {
                when (it) {
                    is Resource.Error -> _uiEvent.send(ShowToast(it.throwable.message))
                    Resource.Loading -> _uiState.emit(Loading)
                    is Resource.Success -> _uiEvent.send(GoToAuthActivity)
                }
            }
        }
    }


    private fun onLanguageChange(currentLanguageTag: String) {

        viewModelScope.launch {
            val result = if (currentLanguageTag == ARABIC_LANGUAGE_TAG) {
                ENGLISH_LANGUAGE_TAG
            } else ARABIC_LANGUAGE_TAG

            _uiEvent.send(ChangeLanguageAndResetApp(result))
        }
    }

    sealed class MoreViewEvent() {
        data class ShowToast(val message: String?) : MoreViewEvent()
        data class ChangeLanguageAndResetApp(val language: String) : MoreViewEvent()
        data object GoToAuthActivity : MoreViewEvent()

    }

    sealed class MoreViewState {
        data object Idle : MoreViewState()

        data class Success(val user: User) : MoreViewState()
        data object Loading : MoreViewState()

    }

    sealed class MoreViewAction {
        data object LogoutClicked : MoreViewAction()
        data class ChangeLanguage(val currentLanguageTag: String) : MoreViewAction()
    }

    companion object {
        const val ARABIC_LANGUAGE_TAG = "ar"
        const val ENGLISH_LANGUAGE_TAG = "en"
    }

}