package com.vectorincng.squareandroidtaskvictor.data

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val squareAppDispatcher: SquareAppDispatcher)

enum class SquareAppDispatcher {
    Main,
    IO,
}
