package com.abdulmughni.personal.openweatherandroid.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.abdulmughni.personal.openweatherandroid.core.data.Resource
import com.abdulmughni.personal.openweatherandroid.core.domain.model.Weather
import com.abdulmughni.personal.openweatherandroid.core.domain.usecase.WeatherUseCase
import kotlinx.coroutines.flow.Flow

class HomeViewModel @ViewModelInject constructor(private val weatherUseCase: WeatherUseCase) : ViewModel() {
    val weather = weatherUseCase.getWeather().asLiveData()
    fun getWeatherGPS(lat: Int, lon: Int) = weatherUseCase.getWeather(lat, lon).asLiveData()
}