package com.abdulmughni.personal.openweatherandroid.core.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    @ColumnInfo (name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "status")
    var status: String? = null,

    @ColumnInfo(name = "detail")
    var detail: String? = null,

    @ColumnInfo(name = "icon")
    var icon: String? = null,

    @ColumnInfo(name = "temperature")
    var temperature: Double? = null,

    @ColumnInfo(name = "tempMin")
    var tempMin: Double? = null,

    @ColumnInfo(name = "tempMax")
    var tempMax: Double? = null,

    @ColumnInfo(name = "feelsLike")
    var feelsLike: Double? = null,

    @ColumnInfo(name = "humidity")
    var humidity: Int? = null,

    @ColumnInfo(name = "windSpeed")
    var windSpeed: Double? = null,

    @ColumnInfo(name = "timezone")
    var timezone: Int? = null,

    @ColumnInfo(name = "city_name")
    var cityName: String? = null
) : Parcelable