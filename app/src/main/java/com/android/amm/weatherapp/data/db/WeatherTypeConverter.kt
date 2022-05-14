package com.android.amm.weatherapp.data.db

import androidx.room.TypeConverter
import com.android.amm.weatherapp.models.DailyForecastWeather
import com.android.amm.weatherapp.models.Temp
import com.android.amm.weatherapp.models.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class WeatherTypeConverter {
    @TypeConverter
    fun stringToList(value: String): List<DailyForecastWeather> {
        val listType = object : TypeToken<List<DailyForecastWeather>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun listToString(list: List<DailyForecastWeather>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun tempToString(temp: Temp): String = Gson().toJson(temp)

    @TypeConverter
    fun stringToTemp(string: String): Temp = Gson().fromJson(string, Temp::class.java)

    @TypeConverter
    fun weatherToString(weather: List<Weather>): String = Gson().toJson(weather)

    @TypeConverter
    fun stringToWeather(string: String): List<Weather> {
        val listType = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(string, listType)
    }
}