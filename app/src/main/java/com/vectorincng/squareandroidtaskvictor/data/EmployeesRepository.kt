package com.vectorincng.squareandroidtaskvictor.data

import android.util.Log
import com.vectorincng.squareandroidtaskvictor.network.EmployeeFetcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class EmployeesRepository @Inject constructor(
    private val employeeFetcher: EmployeeFetcher,
    @Dispatcher(SquareAppDispatcher.Main) mainDispatcher: CoroutineDispatcher
) {
    private var refreshingJob: Job? = null

    private val scope = CoroutineScope(mainDispatcher)

    suspend fun updatePodcasts(force: Boolean) {
        if (refreshingJob?.isActive == true) {
            refreshingJob?.join()
        } else if (force) {

            refreshingJob = scope.launch {
                // Now fetch the podcasts, and add each to each store
                employeeFetcher().collect { state ->
                    when(state){
                        is EmployeeFetcher.EmployeeDataResponse.Error -> {
                            Log.d("Hi","${state.throwable?.message}")

                        }
                        is EmployeeFetcher.EmployeeDataResponse.Success -> {
                            Log.d("Hi",state.employees.size.toString())
                        }
                        null -> {

                        }
                    }
                }
            }
        }
    }
}
