package com.abdulmughni.personal.openweatherandroid.core.domain.usecase

import com.abdulmughni.personal.openweatherandroid.core.data.Resource
import com.abdulmughni.personal.openweatherandroid.core.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherUseCase {
    fun getWeather(): Flow<Resource<Weather>>
    fun getWeather(lat: Int, lon: Int): Flow<Resource<Weather>>
}