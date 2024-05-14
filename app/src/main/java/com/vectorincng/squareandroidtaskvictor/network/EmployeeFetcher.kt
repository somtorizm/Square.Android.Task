package com.vectorincng.squareandroidtaskvictor.network

import coil.network.HttpException
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.vectorincng.squareandroidtaskvictor.data.Dispatcher
import com.vectorincng.squareandroidtaskvictor.data.EmployeesResponse
import com.vectorincng.squareandroidtaskvictor.data.SquareAppDispatcher
import com.vectorincng.squareandroidtaskvictor.utils.Extensions
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
        return flow {
            emit(fetchJson())
        }.catch { e ->
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
        return this?.let { json ->
            try {
                val employees = Gson().fromJson(json, EmployeesResponse::class.java)
                if (employees.employees.isEmpty()) return EmployeeDataResponse.Error(Throwable("No Employee data found"))

                val transformedEmployees = employees.employees.map { employee ->
                    EmployeeDataResponse.Employee(
                        employee.id ?:  throw IllegalArgumentException(),
                        employee.name ?:  throw IllegalArgumentException(),
                        employee.imageUrl ?:  throw IllegalArgumentException(),
                        employee.biography ?:  throw IllegalArgumentException(),
                        employee.team ?:  throw IllegalArgumentException(),
                        employee.employeeType?:  throw IllegalArgumentException()
                    )
                }
                EmployeeDataResponse.Success(transformedEmployees)
            } catch (e: JsonParseException) {
                EmployeeDataResponse.Error(Throwable("Failed to parse JSON"))
            } catch (e: IllegalArgumentException) {
                EmployeeDataResponse.Error(Throwable("Malformed Response data"))
            }

        } ?: EmployeeDataResponse.Error(Throwable("No data found"))
    }

    sealed class EmployeeDataResponse {
        data class Success(
            val employees: List<Employee>
        ): EmployeeDataResponse()

        data class Error(
            val throwable: Throwable?,
        ) : EmployeeDataResponse()

        data class Employee (
            val id: String,
            val name: String,
            val imageUrl: String = "",
            val biography: String,
            val team: String,
            val employeeType: Extensions.EmployeeType
        )
    }

    companion object {
        private const val BASE_URL = "https://s3.amazonaws.com/sq-mobile-interview/employees.json"
    }
}


