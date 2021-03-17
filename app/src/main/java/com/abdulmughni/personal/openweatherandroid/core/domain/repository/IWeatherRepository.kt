package com.abdulmughni.personal.openweatherandroid.core.domain.repository

import com.abdulmughni.personal.openweatherandroid.core.data.Resource
import com.abdulmughni.personal.openweatherandroid.core.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    //fun getCurrentWeather(): Flow<Resource<Weather>>
    fun getCurrentWeather(lat: Double, lon: Double): Flow<Resource<Weather>>
}