package com.android.amm.weatherapp.view.weatherlist

import androidx.lifecycle.*
import com.android.amm.weatherapp.data.repository.CityListRepository
import com.android.amm.weatherapp.data.repository.WeatherListRepository
import com.android.amm.weatherapp.models.CityModel
import com.android.amm.weatherapp.models.DailyForecastWeather
import com.android.amm.weatherapp.models.WeatherListUiState
import com.android.amm.weatherapp.utils.Result
import com.android.amm.weatherapp.utils.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WeatherListViewModel @Inject constructor(
    private val weatherListRepository: WeatherListRepository,
    private val cityListRepository: CityListRepository
) : ViewModel() {
    private val weatherResultMediator = MediatorLiveData<WeatherListUiState>()

    val weatherListUiState: LiveData<WeatherListUiState?> =
        Transformations.map(weatherResultMediator) {
            it
        }

    private val cityResultMediator = MediatorLiveData<List<CityModel>>()

    val cityListResult: LiveData<List<CityModel>?> =
        Transformations.map(cityResultMediator) {
            it
        }

    init {
        insertCityList()
    }

    private fun insertCityList() {
        viewModelScope.launch {
            cityListRepository.insertCityList()
        }
    }

    fun getCityList(name: String) {
        viewModelScope.launch {
            cityListRepository.getCityList(name).collect {
                it.successOr(null)?.let { response ->
                    cityResultMediator.value = response
                }
            }
        }
    }

    fun getWeatherList(cityModel: CityModel) {
        viewModelScope.launch {
            weatherListRepository.getWeatherList(cityModel.lat, cityModel.lng).collect {
                when (it) {
                    is Result.Success<List<DailyForecastWeather>> -> {
                        it.successOr(null)?.let { response ->
                            weatherResultMediator.value = WeatherListUiState(
                                cityName = cityModel.name,
                                weatherList = response,
                                isWeatherListEmpty = response.isEmpty()
                            )
                        }
                    }
                    is Result.Error -> {
                        weatherResultMediator.value = WeatherListUiState(
                            cityName = cityModel.name,
                            weatherList = null,
                            isWeatherListEmpty = true,
                            isLoading = false,
                            errorMessage = it.exception.message
                        )
                    }
                    is Result.Empty -> {
                        weatherResultMediator.value = WeatherListUiState(
                            cityName = cityModel.name,
                            weatherList = null,
                            isWeatherListEmpty = true, isLoading = false, errorMessage = null
                        )
                    }
                    is Result.Loading -> {
                        weatherResultMediator.value = WeatherListUiState(
                            cityName = cityModel.name,
                            weatherList = null,
                            isWeatherListEmpty = true, isLoading = true, errorMessage = null
                        )
                    }
                }
            }
        }

    }
}
