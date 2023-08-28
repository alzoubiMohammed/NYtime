package com.deloitte.mostpopular.ui.home.dashboard


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deloitte.mostpopular.data.model.News
import com.deloitte.mostpopular.data.network.Resource
import com.deloitte.mostpopular.domain.usecase.GetMostPopularViewUseCase
import com.deloitte.mostpopular.domain.usecase.SearchByTitleUseCase
import com.deloitte.mostpopular.ui.home.dashboard.DashboardViewModel.DashboardViewEvent.*
import com.deloitte.mostpopular.ui.home.dashboard.DashboardViewModel.DashboardViewState.*
import com.deloitte.mostpopular.ui.home.dashboard.filter.FilterBottomSheetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val getMostPopularViewUseCase: GetMostPopularViewUseCase,
    private val searchByTitleUseCase: SearchByTitleUseCase
) :
    ViewModel() {



    private var selectedPeriod:String = FilterBottomSheetViewModel.FilterType.DAY.period

    init {
        getMostPopularView()
    }

    private val _uiEvent = Channel<DashboardViewEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow<DashboardViewState>(Idle)
    val uiState = _uiState.asStateFlow()


    fun processUiAction(action: DashboardViewAction){
        viewModelScope.launch {
            when(action){
                DashboardViewAction.OnRefresh -> getMostPopularView()
                is DashboardViewAction.Search -> doSearch(action.query)
                DashboardViewAction.FilterClicked -> _uiEvent.send(OpenFilter)
                is DashboardViewAction.NewsDetailClicked -> _uiEvent.send(OpenNewsDetails(action.news))
                is DashboardViewAction.FilterApply -> {
                    selectedPeriod=action.period
                    getMostPopularView()
                }
            }
        }
    }

    private fun doSearch(title:String) {
        viewModelScope.launch(IO) {
            searchByTitleUseCase.execute(title).collect{
                _uiState.emit(Success(it))
            }
        }

    }

    private fun getMostPopularView() {

        viewModelScope.launch {
            _uiState.emit(Idle)
            getMostPopularViewUseCase.execute(selectedPeriod).collect {
                when (it) {
                    is Resource.Error -> {
                        _uiEvent.send(ShowToast(it.throwable.message))
                        _uiState.emit(Idle)
                    }
                    Resource.Loading -> _uiState.emit(Loading)
                    is Resource.Success -> _uiState.emit(Success(it.data))
                }
            }
        }
    }


    sealed class DashboardViewEvent {
        data object OpenFilter : DashboardViewEvent()
        data class OpenNewsDetails(val news: News) : DashboardViewEvent()
        data class ShowToast(val message: String?) : DashboardViewEvent()

    }

    sealed class DashboardViewState {
        data object Idle : DashboardViewState()
        data object Loading : DashboardViewState()
        data class Success(val newsList: List<News>) : DashboardViewState()

    }

    sealed class DashboardViewAction {
        data object OnRefresh:DashboardViewAction()
        data object FilterClicked:DashboardViewAction()
        data class Search(val query:String):DashboardViewAction()
        data class FilterApply(val period:String):DashboardViewAction()
        data class NewsDetailClicked(val news: News):DashboardViewAction()

    }


}