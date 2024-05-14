package com.vectorincng.squareandroidtaskvictor.api

import com.vectorincng.squareandroidtaskvictor.data.EmployeesResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface EmployeeService {
    @GET("sq-mobile-interview/employees")
    suspend fun retrieveEmployees(

    ): EmployeesResponse

    companion object {
        private const val BASE_URL = "https://s3.amazonaws.com"

        fun create(): EmployeeService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(EmployeeService::class.java)
        }
    }
}