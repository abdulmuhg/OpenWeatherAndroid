package com.abdulmughni.personal.openweatherandroid.core.utils

import com.abdulmughni.personal.openweatherandroid.core.data.source.local.entity.WeatherEntity
import com.abdulmughni.personal.openweatherandroid.core.data.source.remote.response.WeatherResponse
import com.abdulmughni.personal.openweatherandroid.core.domain.model.Weather

object DataMapper {

    fun mapResponsesToEntities(input: WeatherResponse?, lat: Double, lon: Double): WeatherEntity = WeatherEntity(
        id = input?.id!!,
        lat = lat,
        lon = lon,
        status = input.weather?.get(0)?.main,
        detail = input.weather?.get(0)?.description,
        icon = input.weather?.get(0)?.icon!!,
        temperature = input.main?.temp!!,
        tempMin = input.main.tempMin!!,
        tempMax = input.main.tempMax!!,
        feelsLike = input.main.feelsLike!!,
        humidity = input.main.humidity!!,
        windSpeed = input.wind?.speed!!,
        timezone = input.timezone!!,
        cityName = input.name!!,
        sunrise = input.sys?.sunrise!!,
        sunset = input.sys.sunset!!
    )

    fun mapEntitiesToDomain(inputs: WeatherEntity?): Weather =
        Weather(
            id = inputs?.id,
            lat = inputs?.lat,
            lon = inputs?.lon,
            status = inputs?.status,
            detail = inputs?.detail,
            icon = inputs?.icon,
            temperature = inputs?.temperature,
            tempMin = inputs?.tempMin,
            tempMax = inputs?.tempMax,
            feelsLike = inputs?.feelsLike,
            humidity = inputs?.humidity,
            windSpeed = inputs?.windSpeed,
            timezone = inputs?.timezone,
            cityName = inputs?.cityName,
            sunrise = inputs?.sunrise,
            sunset = inputs?.sunset
        )

    fun mapDomainToEntity(input: Weather) = WeatherEntity(
        id = input.id!!,
        status = input.status,
        detail = input.detail,
        icon = input.icon,
        temperature = input.temperature,
        tempMin = input.tempMin,
        tempMax = input.tempMax,
        feelsLike = input.feelsLike,
        humidity = input.humidity,
        windSpeed = input.windSpeed,
        timezone = input.timezone,
        cityName = input.cityName
    )
}