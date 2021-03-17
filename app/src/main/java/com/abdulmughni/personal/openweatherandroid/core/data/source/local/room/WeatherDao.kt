package com.abdulmughni.personal.openweatherandroid.core.data.source.local.room

import androidx.room.*
import com.abdulmughni.personal.openweatherandroid.core.data.source.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE lat LIKE :latitude AND lon LIKE :lon LIMIT 1")
    fun getWeather(latitude: Double, lon: Double): Flow<WeatherEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherList: WeatherEntity)

    @Update
    fun updateWeather(weather: WeatherEntity)
}