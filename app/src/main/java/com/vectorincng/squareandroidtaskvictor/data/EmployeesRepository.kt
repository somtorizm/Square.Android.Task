package com.vectorincng.squareandroidtaskvictor.data

import com.vectorincng.squareandroidtaskvictor.network.EmployeeFetcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface EmployeesRepository {
   suspend fun updateEmployeeList(force: Boolean) : Flow<EmployeeFetcher.EmployeeDataResponse>
}

class EmployeesRepositoryImpl @Inject constructor(
    private val employeeFetcher: EmployeeFetcher,
    @Dispatcher(SquareAppDispatcher.Main) mainDispatcher: CoroutineDispatcher
): EmployeesRepository {
    private var refreshingJob: Job? = null

    private val scope = CoroutineScope(mainDispatcher)
    override suspend fun updateEmployeeList(force: Boolean): Flow<EmployeeFetcher.EmployeeDataResponse> {
        if (refreshingJob?.isActive == true) {
            refreshingJob?.join()
        } else if (force) {

            refreshingJob = scope.launch {
                // Now fetch the podcasts, and add each to each store
                employeeFetcher.invoke()
            }

        }
        return  employeeFetcher.invoke()
    }

}
