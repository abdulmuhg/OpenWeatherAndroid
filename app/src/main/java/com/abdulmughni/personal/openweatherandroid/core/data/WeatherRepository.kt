package com.abdulmughni.personal.openweatherandroid.core.data

import com.abdulmughni.personal.openweatherandroid.core.data.source.local.LocalDataSource
import com.abdulmughni.personal.openweatherandroid.core.data.source.remote.RemoteDataSource
import com.abdulmughni.personal.openweatherandroid.core.data.source.remote.network.ApiResponse
import com.abdulmughni.personal.openweatherandroid.core.data.source.remote.response.WeatherResponse
import com.abdulmughni.personal.openweatherandroid.core.domain.model.Weather
import com.abdulmughni.personal.openweatherandroid.core.domain.repository.IWeatherRepository
import com.abdulmughni.personal.openweatherandroid.core.utils.AppExecutors
import com.abdulmughni.personal.openweatherandroid.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IWeatherRepository {

    override fun getCurrentWeather(lat: Double, lon: Double): Flow<Resource<Weather>> =
        object : NetworkBoundResource<Weather, WeatherResponse>(appExecutors) {
            override fun loadFromDB(): Flow<Weather> {
                return localDataSource.getWeather(lat, lon).mapNotNull {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: Weather?): Boolean =
                true

            override suspend fun createCall(): Flow<ApiResponse<WeatherResponse>> =
                remoteDataSource.getWeather(lat, lon)

            override suspend fun saveCallResult(data: WeatherResponse) {
                val weather = DataMapper.mapResponsesToEntities(data, lat, lon)
                localDataSource.insertWeather(weather)
            }
        }.asFlow()

    override fun getCurrentWeather(
        lat: Double,
        lon: Double,
        units: String
    ): Flow<Resource<Weather>> =
        object : NetworkBoundResource<Weather, WeatherResponse>(appExecutors) {
            override fun loadFromDB(): Flow<Weather> {
                return localDataSource.getWeather(lat, lon).mapNotNull {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: Weather?): Boolean =
                true

            override suspend fun createCall(): Flow<ApiResponse<WeatherResponse>> =
                remoteDataSource.getWeather(lat, lon, units)

            override suspend fun saveCallResult(data: WeatherResponse) {
                val weather = DataMapper.mapResponsesToEntities(data, lat, lon)
                localDataSource.insertWeather(weather)
            }
        }.asFlow()
}