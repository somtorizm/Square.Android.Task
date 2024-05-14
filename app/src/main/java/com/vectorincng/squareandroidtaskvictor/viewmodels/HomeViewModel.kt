package com.vectorincng.squareandroidtaskvictor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vectorincng.squareandroidtaskvictor.data.EmployeesRepository
import com.vectorincng.squareandroidtaskvictor.network.EmployeeFetcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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
                employeesRepository.updateEmployeeList(force).collect { state ->
                    when(state) {
                        is EmployeeFetcher.EmployeeDataResponse.Error -> {
                            _state.value = HomeScreenUiState.Error(state.throwable?.message)
                        }
                        is EmployeeFetcher.EmployeeDataResponse.Success -> {
                            _state.value = HomeScreenUiState.Ready(state.employees)
                        }
                    }
                }
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

    data class Ready(
        val featuredEmployees: List<EmployeeFetcher.EmployeeDataResponse.Employee> = emptyList(),
    ) : HomeScreenUiState

}
