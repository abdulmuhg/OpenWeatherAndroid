package com.abdulmughni.personal.openweatherandroid.core.data.source.remote

import android.util.Log
import com.abdulmughni.personal.openweatherandroid.core.data.source.remote.network.ApiResponse
import com.abdulmughni.personal.openweatherandroid.core.data.source.remote.network.ApiService
import com.abdulmughni.personal.openweatherandroid.core.data.source.remote.response.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getWeather(lat: Double, lon: Double): Flow<ApiResponse<WeatherResponse>> {
        return flow {
            try {
                val response = apiService.getWeather(ApiService.API_KEY, lat, lon)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getWeather(lat: Double, lon: Double, units: String): Flow<ApiResponse<WeatherResponse>> {
        return flow {
            try {
                val response = apiService.getWeather(ApiService.API_KEY, lat, lon, units)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}