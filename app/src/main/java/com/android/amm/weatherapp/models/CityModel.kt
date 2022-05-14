package com.android.amm.weatherapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityModel(
    @PrimaryKey val name: String,
    val lat: Double,
    val lng: Double
)
