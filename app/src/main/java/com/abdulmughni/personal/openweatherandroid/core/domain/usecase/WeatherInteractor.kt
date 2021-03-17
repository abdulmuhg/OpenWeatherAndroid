package com.abdulmughni.personal.openweatherandroid.core.domain.usecase

import com.abdulmughni.personal.openweatherandroid.core.data.Resource
import com.abdulmughni.personal.openweatherandroid.core.domain.model.Weather
import com.abdulmughni.personal.openweatherandroid.core.domain.repository.IWeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherInteractor @Inject constructor(private val weatherRepository: IWeatherRepository) : WeatherUseCase {
    //override fun getWeather(): Flow<Resource<Weather>> = weatherRepository.getCurrentWeather()
    override fun getWeather(lat: Double, lon: Double): Flow<Resource<Weather>> = weatherRepository.getCurrentWeather(lat, lon)
}