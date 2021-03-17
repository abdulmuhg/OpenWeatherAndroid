package com.abdulmughni.personal.openweatherandroid.core.di

import com.abdulmughni.personal.openweatherandroid.core.data.WeatherRepository
import com.abdulmughni.personal.openweatherandroid.core.domain.repository.IWeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideRepository(weatherRepository: WeatherRepository): IWeatherRepository
}