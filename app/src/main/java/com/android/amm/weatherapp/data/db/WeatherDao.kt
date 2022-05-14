package com.android.amm.weatherapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.amm.weatherapp.models.CityModel
import com.android.amm.weatherapp.models.DailyForecastWeather

@Dao
interface WeatherDao {
    @Query("SELECT * FROM DailyForecastWeather")
    fun getWeatherList(): List<DailyForecastWeather>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertWeathers(weatherModel: List<DailyForecastWeather>)

    @Query("DELETE FROM DailyForecastWeather")
    fun deleteWeathers()

    @Query("SELECT * FROM CityModel WHERE name LIKE :cityName || '%'")
    fun getCityList(cityName: String): List<CityModel>
}