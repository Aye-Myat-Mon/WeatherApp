package com.android.amm.weatherapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.amm.weatherapp.R
import com.android.amm.weatherapp.models.DailyForecastWeather
import com.android.amm.weatherapp.view.weatherdetail.WeatherDetailFragment
import com.android.amm.weatherapp.view.weatherlist.WeatherListFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}