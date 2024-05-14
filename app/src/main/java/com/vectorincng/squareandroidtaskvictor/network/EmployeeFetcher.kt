package com.vectorincng.squareandroidtaskvictor.network

import coil.network.HttpException
import com.google.gson.Gson
import com.vectorincng.squareandroidtaskvictor.data.Dispatcher
import com.vectorincng.squareandroidtaskvictor.data.EmployeesResponse
import com.vectorincng.squareandroidtaskvictor.data.SquareAppDispatcher
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * A class which fetches Json response.
 *
 * @param okHttpClient [OkHttpClient] to use for network requests
 * @param ioDispatcher [CoroutineDispatcher] to use for running fetch requests.
 */
class EmployeeFetcher @Inject constructor(
    private val okHttpClient: OkHttpClient,
    @Dispatcher(SquareAppDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {

    /**
     * implement HTTP caching appropriately.
     * Instead of fetching data on every app open, we instead allow the use of 'stale'
     * network responses (up to 8 hours).
     */
    private val cacheControl by lazy {
        CacheControl.Builder().maxStale(8, TimeUnit.HOURS).build()
    }


    operator fun invoke(): Flow<EmployeeDataResponse> {
        // We use flatMapMerge here to achieve concurrent fetching/parsing of the feeds.
        return flow {
            emit(fetchJson())
        }.catch { e ->
            // If an exception was caught while fetching the json, wrap it in
            // an Error instance.
            emit(EmployeeDataResponse.Error(e))
        }
    }

    private suspend fun fetchJson(): EmployeeDataResponse {
        val request = Request.Builder()
            .url(BASE_URL)
            .cacheControl(cacheControl)
            .build()

        val response = okHttpClient.newCall(request).await()

        // If the network request wasn't successful, throw an exception
        if (!response.isSuccessful) throw HttpException(response)
        return withContext(ioDispatcher) {
            response.body!!.string().toEmployeesResponse()
        }
    }

    private fun String?.toEmployeesResponse(): EmployeeDataResponse {
        this?.let { it ->
            val employees = Gson().fromJson(it, EmployeesResponse::class.java)
            if (employees.employees.isNotEmpty()) {
                return EmployeeDataResponse.Success(
                    employees.employees.map {
                        EmployeeDataResponse.Employee(it.name, it.imageUrl, it.biography, it.team)
                    }
                )
            }
        }

        return EmployeeDataResponse.Error(Throwable("Failed to parse JSON or no employees found"))
    }

    sealed class EmployeeDataResponse {
        data class Success(
            val employees: List<Employee>
        ): EmployeeDataResponse()

        data class Error(
            val throwable: Throwable?,
        ) : EmployeeDataResponse()

        data class Employee (
            val name: String,
            val imageUrl: String = "",
            val biography: String,
            val team: String,
        )
    }

    companion object {
        private const val BASE_URL = "https://s3.amazonaws.com/sq-mobile-interview/employees.json"
    }
}


