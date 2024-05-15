package com.vectorincng.squareandroidtaskvictor.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vectorincng.squareandroidtaskvictor.data.EmployeesRepository
import com.vectorincng.squareandroidtaskvictor.data.Employee
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
    private var employeeList : List<Employee> = emptyList()

    fun refresh(force: Boolean = true) {
        viewModelScope.launch {
            runCatching {
                refreshing.value = true
                _state.value = HomeScreenUiState.Loading

                employeesRepository.updateEmployeeList(force).collect { state ->
                    when(state) {
                        is EmployeeFetcher.EmployeeDataResponse.Error -> {
                            _state.value = HomeScreenUiState.Error(state.throwable?.message)
                        }
                        is EmployeeFetcher.EmployeeDataResponse.Success -> {
                            _state.value = HomeScreenUiState.Ready(state.employees)
                            employeeList = state.employees
                        }
                    }
                }
            }
            refreshing.value = false
        }
    }

    fun sortListName(query: String?) {
        if (state.value is HomeScreenUiState.Ready) {
            val sortedEmployees = query?.let {
                when (it) {
                    "name" -> employeeList.sortedBy { employee -> employee.name }
                    "team" -> employeeList.sortedBy { employee -> employee.team }
                    else -> employeeList.sortedBy { employee -> employee.employeeType }
                }
            } ?: employeeList.sortedBy { employee -> employee.employeeType }

            _state.value = HomeScreenUiState.Ready(sortedEmployees)
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
        val featuredEmployees: List<Employee> = emptyList(),
    ) : HomeScreenUiState
}
