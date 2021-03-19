package com.abdulmughni.personal.openweatherandroid.core.data.source.remote.network

import com.abdulmughni.personal.openweatherandroid.BuildConfig
import com.abdulmughni.personal.openweatherandroid.core.data.source.remote.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    companion object {
        const val API_KEY: String = BuildConfig.API_KEY
    }
    @GET("weather")
    suspend fun getWeather(
        @Query("appid") appId: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): WeatherResponse

    @GET("weather")
    suspend fun getWeather(
            @Query("appid") appId: String,
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            @Query("units") units: String
    ): WeatherResponse
}