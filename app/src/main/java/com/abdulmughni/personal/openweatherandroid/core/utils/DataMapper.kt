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
        cityName = input.name!!
    )

//    fun mapResponsesToEntities(input: WeatherResponse?): WeatherEntity = WeatherEntity(
//        id = 0,
//        status = "Rain",
//        detail = "Heavy Rain",
//        icon = "",
//        temperature = 283.4,
//        tempMin = 283.15,
//        tempMax = 283.15,
//        feelsLike = 283.11,
//        humidity = 88,
//        windSpeed = 2.28,
//        timezone = 32400,
//        cityName = "Shuzenji"
//    )

//    fun mapEntitiesToDomain(inputs: WeatherEntity?): Weather =
//        Weather(
//            id = 0,
//            status = "Rain",
//            detail = "Heavy Rain",
//            icon = "",
//            temperature = 283.4,
//            tempMin = 283.15,
//            tempMax = 283.15,
//            feelsLike = 283.11,
//            humidity = 88,
//            windSpeed = 2.28,
//            timezone = 32400,
//            cityName = "Shuzenji"
//        )

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
            cityName = inputs?.cityName
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