package com.android.amm.weatherapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.amm.weatherapp.models.CityModel
import com.android.amm.weatherapp.models.DailyForecastWeather

@Database(entities = [DailyForecastWeather::class, CityModel::class], version = 1)
@TypeConverters(WeatherTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}