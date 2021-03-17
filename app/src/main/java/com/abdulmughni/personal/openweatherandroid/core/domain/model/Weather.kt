package com.abdulmughni.personal.openweatherandroid.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather(
    var id: Int? = null,
    var lat: Double? = null,
    var lon: Double? = null,
    var status: String? = null,
    var detail: String? = null,
    var icon: String? = null,
    var temperature: Double? = null,
    var tempMin: Double? = null,
    var tempMax: Double? = null,
    var feelsLike: Double? = null,
    var humidity: Int? = null,
    var windSpeed: Double? = null,
    var timezone: Int? = null,
    var cityName: String? = null
) : Parcelable