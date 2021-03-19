package com.example.androiddevchallenge.data

import com.example.androiddevchallenge.common.Either
import com.example.androiddevchallenge.domain.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeoutException

class MainRepository {

    private val fakeData = listOf(
        Either.Right(Weather.CLOUDY),
        Either.Right(Weather.SUNNY),
        Either.Left(TimeoutException()),
    )

    val weather = flow<Either<Throwable, Weather>> {
        delay(1000)
        emit(fakeData.random())
    }.flowOn(Dispatchers.IO)
}
