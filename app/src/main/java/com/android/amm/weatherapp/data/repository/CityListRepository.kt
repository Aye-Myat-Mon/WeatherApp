package com.android.amm.weatherapp.data.repository

import android.app.Application
import android.content.res.AssetManager
import com.android.amm.weatherapp.constant.Constants
import com.android.amm.weatherapp.data.db.WeatherDao
import com.android.amm.weatherapp.models.CityModel
import com.android.amm.weatherapp.models.DailyForecastWeather
import com.android.amm.weatherapp.utils.Result
import com.android.amm.weatherapp.utils.safeApiCall
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityListRepository @Inject constructor(
    private val weatherDao: WeatherDao,
    private val application: Application) {

    suspend fun insertCityList() {
        return withContext(Dispatchers.IO) {
            val cityList = Gson().fromJson(application.assets.readAssetsFile("cities.json"), Array<CityModel>::class.java).asList()
            weatherDao.insertCities(cityList)
        }
    }

    suspend fun getCityList(name: String): Flow<Result<List<CityModel>>> {
        return flow {
            emit(Result.Success(weatherDao.getCityList(name)))
        }.flowOn(Dispatchers.IO)
    }

    private fun AssetManager.readAssetsFile(fileName : String): String = open(fileName).bufferedReader().use{it.readText()}
}