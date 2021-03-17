package com.abdulmughni.personal.openweatherandroid.core.di

import android.content.Context
import androidx.room.Room
import com.abdulmughni.personal.openweatherandroid.core.data.source.local.room.WeatherDao
import com.abdulmughni.personal.openweatherandroid.core.data.source.local.room.WeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): WeatherDatabase = Room.databaseBuilder(
        context,
        WeatherDatabase::class.java,
        "weather.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideWeatherDao(database: WeatherDatabase) : WeatherDao = database.weatherDao()
}