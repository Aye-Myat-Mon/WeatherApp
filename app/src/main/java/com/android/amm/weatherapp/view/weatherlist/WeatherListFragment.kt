package com.android.amm.weatherapp.view.weatherlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.amm.weatherapp.R
import com.android.amm.weatherapp.databinding.FragmentWeatherListBinding
import com.android.amm.weatherapp.models.CityModel
import com.android.amm.weatherapp.models.ErrorMessageModel
import com.android.amm.weatherapp.utils.isNetworkAvailable
import com.android.amm.weatherapp.view.weatherdetail.WeatherDetailFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WeatherListFragment: Fragment() {
    private val viewModel: WeatherListViewModel by activityViewModels()

    private lateinit var weatherListBinding: FragmentWeatherListBinding

    private val weatherListAdapter by lazy {
        WeatherListAdapter {
            val bundle = Bundle()
            bundle.putString(WeatherDetailFragment.key_weather_data, Gson().toJson(it))
            findNavController().navigate(
                R.id.action_weatherListFragment_to_weatherDetailFragment,
                bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        weatherListBinding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return weatherListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        viewModel.cityListResult.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            bindCityRecycler(it)
        }

        viewModel.weatherListUiState.observe(viewLifecycleOwner) {
            if (it == null) return@observe

            if (it.weatherList != null && !it.isWeatherListEmpty) {
                showWeatherList()
                weatherListBinding.tvCityName.text = it.cityName
                weatherListAdapter.setWeatherList(it.weatherList)
            }

            if (it.errorMessage != null) {
                val errMessage = Gson().fromJson(it.errorMessage, ErrorMessageModel::class.java)
                showErrorMessage()
                weatherListBinding.tvNoInternet.text = errMessage.message
            }
        }

        weatherListBinding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                weatherListBinding.rvCity.visibility = View.VISIBLE
                viewModel.getCityList(newText)
                if (newText.isEmpty()) {
                    weatherListBinding.rvCity.visibility = View.GONE
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getCityList(query)
                return false
            }

        })
    }

    private fun initRecyclerView() {
        weatherListBinding.rvWeather.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = weatherListAdapter
        }
    }

    private fun bindCityRecycler(cityList: List<CityModel>) {
        val cityAdapter = CityListAdapter(cityList) {
            if (isNetworkAvailable(requireContext())) {
                weatherListBinding.rvCity.visibility = View.GONE
                viewModel.getWeatherList(it)
            } else {
                showErrorMessage()
                weatherListBinding.tvNoInternet.text = resources.getString(R.string.str_no_internet)
            }
        }
        weatherListBinding.rvCity.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = cityAdapter
        }
    }

    private fun showWeatherList() {
        weatherListBinding.tvNoInternet.visibility = View.GONE
        weatherListBinding.tvCityName.visibility = View.VISIBLE
        weatherListBinding.rvWeather.visibility = View.VISIBLE
        weatherListBinding.divider.visibility = View.VISIBLE
    }

    private fun showErrorMessage() {
        weatherListBinding.tvNoInternet.visibility = View.VISIBLE
        weatherListBinding.rvCity.visibility = View.GONE
        weatherListBinding.tvCityName.visibility = View.GONE
        weatherListBinding.rvWeather.visibility = View.GONE
        weatherListBinding.divider.visibility = View.GONE
    }
}