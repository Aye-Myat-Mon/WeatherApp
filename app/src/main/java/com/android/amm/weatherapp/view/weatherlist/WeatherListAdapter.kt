package com.android.amm.weatherapp.view.weatherlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.amm.weatherapp.R
import com.android.amm.weatherapp.constant.Constants
import com.android.amm.weatherapp.databinding.RowDailyWeatherItemBinding
import com.android.amm.weatherapp.models.DailyForecastWeather
import com.android.amm.weatherapp.models.WeatherUiState
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class WeatherListAdapter(
    private val onItemClickListener: (weather: WeatherUiState) -> Unit
) : RecyclerView.Adapter<WeatherListAdapter.WeatherListViewHolder>() {

    private val weatherList = mutableListOf<DailyForecastWeather>()

    fun setWeatherList(weathers: List<DailyForecastWeather>) {
        weatherList.clear()
        weatherList.addAll(weathers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherListViewHolder {
        return WeatherListViewHolder(
            RowDailyWeatherItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherListViewHolder, position: Int) {
        val weatherObj = weatherList[position]
        val timeD = Date(weatherObj.dt.toLong() * 1000)
        val calendar = Calendar.getInstance()
        calendar.time = timeD

        holder.bind(
            WeatherUiState(
                "${SimpleDateFormat("MMM").format(calendar.time)} ${calendar.get(Calendar.DAY_OF_MONTH)}",
                "${ceil(weatherObj.temp.day).toInt()}${holder.itemView.resources.getString(R.string.str_degree)}",
                "${ceil(weatherObj.temp.night).toInt()}${holder.itemView.context.resources.getString(R.string.str_degree)}",
                weatherObj.weather[0].main,
                weatherObj.weather[0].icon
        ), onItemClickListener)
    }

    override fun getItemCount(): Int {
        return if (weatherList.isEmpty()) weatherList.size else 7
    }

    class WeatherListViewHolder(
        private val weatherListBinding: RowDailyWeatherItemBinding
    ) : RecyclerView.ViewHolder(weatherListBinding.root) {

        fun bind(
            weather: WeatherUiState,
            onItemClickListener: (weather: WeatherUiState) -> Unit
        ) {

            weatherListBinding.apply {
                tvDate.text = weather.date
                tvDayTemp.text = weather.dayTemp
                tvNightTemp.text = weather.nightTemp
                tvWeatherStatus.text = weather.status

                Glide.with(itemView.context)
                    .load(Constants.ICON_BASE_URL + weather.icon + "@2x.png")
                    .into(ivWeatherIcon)

                clWeather.setOnClickListener {
                    onItemClickListener.invoke(weather)
                }
            }
        }

    }

}