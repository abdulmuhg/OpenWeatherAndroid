package com.abdulmughni.personal.openweatherandroid.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.abdulmughni.personal.openweatherandroid.core.domain.usecase.WeatherUseCase

class HomeViewModel @ViewModelInject constructor(private val weatherUseCase: WeatherUseCase) : ViewModel() {
    fun getWeatherGPS(lat: Double, lon: Double) = weatherUseCase.getWeather(lat, lon).asLiveData()
    fun getWeather(lat: Double, lon: Double, units: String) = weatherUseCase.getWeather(lat, lon, units).asLiveData()
}