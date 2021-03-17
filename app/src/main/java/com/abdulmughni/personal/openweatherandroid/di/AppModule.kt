package com.abdulmughni.personal.openweatherandroid.di

import com.abdulmughni.personal.openweatherandroid.core.domain.usecase.WeatherInteractor
import com.abdulmughni.personal.openweatherandroid.core.domain.usecase.WeatherUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@Module
@InstallIn(ActivityComponent::class)
abstract class AppModule {

    @Binds
    abstract fun provideTourismUseCase(weatherInteractor: WeatherInteractor): WeatherUseCase

}