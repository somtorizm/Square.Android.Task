package com.vectorincng.squareandroidtaskvictor.di

import android.content.Context
import coil.ImageLoader
import com.vectorincng.squareandroidtaskvictor.BuildConfig
import com.vectorincng.squareandroidtaskvictor.data.Dispatcher
import com.vectorincng.squareandroidtaskvictor.data.EmployeesRepository
import com.vectorincng.squareandroidtaskvictor.data.EmployeesRepositoryImpl
import com.vectorincng.squareandroidtaskvictor.data.SquareAppDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.LoggingEventListener
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataDiModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient = OkHttpClient.Builder()
        .cache(Cache(File(context.cacheDir, "http_cache"), (20 * 1024 * 1024).toLong()))
        .apply {
            if (BuildConfig.DEBUG) eventListenerFactory(LoggingEventListener.Factory())
        }
        .build()

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context
    ): ImageLoader = ImageLoader.Builder(context)
        // Disable `Cache-Control` header support as some podcast images disable disk caching.
        .respectCacheHeaders(false)
        .build()

    @Provides
    @Dispatcher(SquareAppDispatcher.IO)
    @Singleton
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(SquareAppDispatcher.Main)
    @Singleton
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @Singleton
    fun provideEmployeeRepository(employeesRepository: EmployeesRepositoryImpl) : EmployeesRepository {
        return  employeesRepository
    }
}