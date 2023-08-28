package com.deloitte.mostpopular.ui.home.dashboard.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class FilterBottomSheetViewModel : ViewModel() {
    private var selectedFilterType: FilterType? = null

    private val _uiEvent = Channel<FilterViewEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun processUiAction(action: FilterViewAction) {
        viewModelScope.launch {
            when (action) {
                FilterViewAction.CloseClicked -> _uiEvent.send(FilterViewEvent.DismissFilter)
                is FilterViewAction.FilterSelected -> selectedFilterType = action.filterType
                FilterViewAction.ApplyClicked -> _uiEvent.send(
                    FilterViewEvent.SetResult(
                        selectedFilterType
                    )
                )
            }
        }

    }


    sealed class FilterViewEvent {

        data object DismissFilter : FilterViewEvent()
        data class SetResult(val filterType: FilterType?) : FilterViewEvent()

    }

    sealed class FilterViewAction {
        data object CloseClicked : FilterViewAction()
        data object ApplyClicked : FilterViewAction()
        data class FilterSelected(val filterType: FilterType) :
            FilterViewAction()

    }

    enum class FilterType(val period:String){
        DAY("1"),
        WEEK("7"),
        MONTH("30");




        companion object {
            fun getType(position:Int):FilterType{
                return when(position){
                    0->DAY
                    1->WEEK
                    2->MONTH
                    else -> throw RuntimeException("filter in $position not found")
                }
            }
        }
    }


}