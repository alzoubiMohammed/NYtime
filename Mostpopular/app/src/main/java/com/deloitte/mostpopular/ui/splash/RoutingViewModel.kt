package com.deloitte.mostpopular.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deloitte.mostpopular.data.model.User
import com.deloitte.mostpopular.data.network.Resource
import com.deloitte.mostpopular.domain.usecase.IsLoggedInUseCase
import com.deloitte.mostpopular.ui.splash.RoutingViewModel.StartViewEvent.*
import com.deloitte.mostpopular.ui.splash.RoutingViewModel.StartViewState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutingViewModel @Inject constructor(private val isLoggedInUseCase: IsLoggedInUseCase) :
    ViewModel() {

    init {
        checkUserLoggedIn()
    }

    private val _uiEvent = Channel <StartViewEvent>()
     val uiEvent =_uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow<StartViewState>(Idle)
     val uiState =_uiState

     fun checkUserLoggedIn() {
        viewModelScope.launch {
            isLoggedInUseCase.execute().collect {
                when (it) {
                    is Resource.Error ->_uiEvent.send(ShowToast(it.throwable.message))
                    Resource.Loading -> _uiState.emit(Loading)
                    is Resource.Success -> handleSuccess(it.data)
                }

            }
        }
    }

    private fun handleSuccess(user: User?) {
        val result = if(user!=null){
            OpenHomeActivity
        }else{
            OpenAuthActivity
        }
        viewModelScope.launch {
            _uiEvent.send(result)
        }
    }




    sealed class StartViewState(){
        data object Loading:StartViewState()
        data object Idle : StartViewState()

    }

    sealed class StartViewEvent(){
        data object OpenAuthActivity:StartViewEvent()
        data object OpenHomeActivity:StartViewEvent()
        data class ShowToast(val message: String?):StartViewEvent()
    }






}