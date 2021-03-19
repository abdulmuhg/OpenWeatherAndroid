package com.abdulmughni.personal.openweatherandroid.core.domain.usecase

import com.abdulmughni.personal.openweatherandroid.core.data.Resource
import com.abdulmughni.personal.openweatherandroid.core.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherUseCase {
    //fun getWeather(): Flow<Resource<Weather>>
    fun getWeather(lat: Double, lon: Double): Flow<Resource<Weather>>
    fun getWeather(lat: Double, lon: Double, units: String): Flow<Resource<Weather>>
}