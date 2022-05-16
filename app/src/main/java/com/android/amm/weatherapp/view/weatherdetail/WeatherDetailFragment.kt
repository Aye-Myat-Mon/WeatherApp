package com.android.amm.weatherapp.view.weatherdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.amm.weatherapp.constant.Constants
import com.android.amm.weatherapp.databinding.FragmentWeatherDetailBinding
import com.android.amm.weatherapp.models.WeatherUiState
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherDetailFragment : Fragment() {

    private var binding: FragmentWeatherDetailBinding? = null
    private val weatherDetailBinding get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherDetailBinding.inflate(inflater, container, false)
        return weatherDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weather = Gson().fromJson(
            arguments?.getString(key_weather_data),
            WeatherUiState::class.java
        )

        weatherDetailBinding.apply {
            tvDetailDate.text = weather.date
            tvDetailDayTemp.text = weather.dayTemp
            tvDetailNightTemp.text = weather.nightTemp
            tvDetailStatus.text = weather.status

            Glide.with(requireActivity())
                .load(Constants.ICON_BASE_URL + weather.icon + "@2x.png")
                .into(ivDetailWeatherIcon)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val key_weather_data = "key_weather_data"
    }
}