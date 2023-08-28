package com.deloitte.mostpopular.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableLiveData<NewsDetailsViewState>()
    val uiState: LiveData<NewsDetailsViewState> = _uiState

    fun init(date: String, description: String, imageUrl: String) {
        val newState = NewsDetailsViewState(date, description, imageUrl)
        _uiState.value = newState
    }
}