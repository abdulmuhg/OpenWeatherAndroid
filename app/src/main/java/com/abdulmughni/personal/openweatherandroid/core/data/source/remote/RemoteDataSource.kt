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

    suspend fun getWeather(): Flow<ApiResponse<WeatherResponse>> {
        return flow {
            try {
                val response = apiService.getWeather(appId = "fdf871cedaf3413c6a23230372c30a02", 70, 140)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getWeather(lat: Int, lon: Int): Flow<ApiResponse<WeatherResponse>> {
        return flow {
            try {
                val response = apiService.getWeather(appId = "fdf871cedaf3413c6a23230372c30a02", lat, lon)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}