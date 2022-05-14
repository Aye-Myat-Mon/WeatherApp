package com.android.amm.weatherapp.data.repository

import com.android.amm.weatherapp.constant.Constants
import com.android.amm.weatherapp.data.api.WeatherApi
import com.android.amm.weatherapp.data.db.WeatherDao
import com.android.amm.weatherapp.models.DailyForecastWeather
import com.android.amm.weatherapp.utils.Result
import com.android.amm.weatherapp.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class WeatherListRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao
) {
    suspend fun getWeatherList(
        lat: Double,
        lon: Double
    ): Flow<Result<List<DailyForecastWeather>>> {
        return flow {
            emit(fetchWeatherCached())
            emit(Result.Loading)

            when (val response = safeApiCall { weatherApi.getWeatherList(lat, lon, Constants.API_KEY, "metric", "minutely,hourly,current") }) {
                is Result.Success -> {
                    Timber.d("amm: weather repo ${response.data} ${response.data.daily}")
                    if (response.data.daily != null) {
                        weatherDao.deleteWeathers()
                        weatherDao.insertWeathers(response.data.daily)
                    }
                    emit(Result.Success(weatherDao.getWeatherList()))
                }
                is Result.Error -> {
                    Timber.d("amm: weather repo ${response.exception}")
                    emit(Result.Error(response.exception))
                }
                else -> {
                    emit(Result.Empty)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun fetchWeatherCached(): Result<List<DailyForecastWeather>> =
         Result.Success(weatherDao.getWeatherList())
}