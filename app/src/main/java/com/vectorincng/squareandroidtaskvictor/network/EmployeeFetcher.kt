package com.vectorincng.squareandroidtaskvictor.network

import android.util.Log
import coil.network.HttpException
import com.google.gson.Gson
import com.vectorincng.squareandroidtaskvictor.data.Dispatcher
import com.vectorincng.squareandroidtaskvictor.data.EmployeesResponse
import com.vectorincng.squareandroidtaskvictor.data.SquareAppDispatcher
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * A class which fetches some selected podcast RSS feeds.
 *
 * @param okHttpClient [OkHttpClient] to use for network requests
 * @param ioDispatcher [CoroutineDispatcher] to use for running fetch requests.
 */
class EmployeeFetcher @Inject constructor(
    private val okHttpClient: OkHttpClient,
    @Dispatcher(SquareAppDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {

    /**
     * It seems that most podcast hosts do not implement HTTP caching appropriately.
     * Instead of fetching data on every app open, we instead allow the use of 'stale'
     * network responses (up to 8 hours).
     */
    private val cacheControl by lazy {
        CacheControl.Builder().maxStale(8, TimeUnit.HOURS).build()
    }

    /**
     * Returns a [Flow] which fetches each podcast feed and emits it in turn.
     *
     * The feeds are fetched concurrently, meaning that the resulting emission order may not
     * match the order of [feedUrls].
     */

    operator fun invoke(): Flow<EmployeeDataResponse> {
        // We use flatMapMerge here to achieve concurrent fetching/parsing of the feeds.
        return flow {
            emit(fetchJson())
        }.catch { e ->
            // If an exception was caught while fetching the podcast, wrap it in
            // an Error instance.
            emit(EmployeeDataResponse.Error(e))
        }
    }

    private suspend fun fetchJson(): EmployeeDataResponse {
        val request = Request.Builder()
            .url(Companion.BASE_URL)
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
        this?.let {
            val employees = Gson().fromJson(it, EmployeesResponse::class.java)
            Log.d("Employee", employees.employees.size.toString())

            if (employees.employees.isNotEmpty()) {

                val employee = employees.employees[0]
                Log.d("Employee", employee.name)
                return EmployeeDataResponse.Success(
                    employee.name,
                    employee.imageUrl,
                    employee.biography,
                    employee.team
                )
            }
        }

        return EmployeeDataResponse.Error(Throwable("Failed to parse JSON or no employees found"))
    }

    sealed class EmployeeDataResponse {
        data class Error(
            val throwable: Throwable?,
        ) : EmployeeDataResponse()

        data class Success(
            val name: String,
            val imageUrl: String = "",
            val biography: String,
            val team: String,
        ) : EmployeeDataResponse()
    }

    companion object {
        private const val BASE_URL = "https://s3.amazonaws.com/sq-mobile-interview/employees.json"
    }
}


