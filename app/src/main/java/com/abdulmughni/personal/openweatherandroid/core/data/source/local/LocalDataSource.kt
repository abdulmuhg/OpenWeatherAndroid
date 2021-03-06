package com.abdulmughni.personal.openweatherandroid.core.data.source.local

import com.abdulmughni.personal.openweatherandroid.core.data.source.local.entity.WeatherEntity
import com.abdulmughni.personal.openweatherandroid.core.data.source.local.room.WeatherDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val weatherDao: WeatherDao) {
    fun getWeather(lat: Double, lon: Double): Flow<WeatherEntity> = weatherDao.getWeather(lat, lon)
    suspend fun insertWeather(weather: WeatherEntity) =
        weatherDao.insertWeather(weather)
}