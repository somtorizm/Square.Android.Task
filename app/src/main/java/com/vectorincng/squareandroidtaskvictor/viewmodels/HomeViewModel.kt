package com.vectorincng.squareandroidtaskvictor.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vectorincng.squareandroidtaskvictor.data.EmployeesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val employeesRepository: EmployeesRepository,
) : ViewModel() {
    private val refreshing = MutableStateFlow(false)
    private val _state = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Loading)

    fun refresh(force: Boolean = true) {
        viewModelScope.launch {
            runCatching {
                refreshing.value = true
                employeesRepository.updatePodcasts(force)
            }
            // TODO: look at result of runCatching and show any errors

            refreshing.value = false
        }
    }

    val state: StateFlow<HomeScreenUiState>
        get() = _state

    init {
        viewModelScope.launch {
           refresh()
        }

        refresh(force = false)
    }
}

sealed interface HomeScreenUiState {
    data object Loading : HomeScreenUiState

    data class Error(
        val errorMessage: String? = null
    ) : HomeScreenUiState

}
