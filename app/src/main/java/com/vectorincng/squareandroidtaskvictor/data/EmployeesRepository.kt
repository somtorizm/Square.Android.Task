package com.vectorincng.squareandroidtaskvictor.data

import android.util.Log
import com.vectorincng.squareandroidtaskvictor.api.EmployeeService
import com.vectorincng.squareandroidtaskvictor.network.EmployeeFetcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
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
                employeeFetcher()
                    .filter { it is EmployeeFetcher.EmployeeDataResponse.Success }
                    .map { it as EmployeeFetcher.EmployeeDataResponse.Success }
                    .collect { it ->
                        Log.d("Result", it.name)
                    }
            }
        }
    }
}
