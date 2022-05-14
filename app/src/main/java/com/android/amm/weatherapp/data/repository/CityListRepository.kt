package com.android.amm.weatherapp.data.repository

import com.android.amm.weatherapp.data.db.WeatherDao
import com.android.amm.weatherapp.models.CityModel
import com.android.amm.weatherapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CityListRepository @Inject constructor(private val weatherDao: WeatherDao) {

    suspend fun getCityList(name: String): Flow<Result<List<CityModel>>> {
        return flow {
            emit(Result.Success(weatherDao.getCityList(name)))
        }.flowOn(Dispatchers.IO)
    }
}