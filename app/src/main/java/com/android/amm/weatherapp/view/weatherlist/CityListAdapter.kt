package com.android.amm.weatherapp.view.weatherlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.amm.weatherapp.databinding.RowCityItemBinding
import com.android.amm.weatherapp.models.CityModel

class CityListAdapter(
    private val cityList: List<CityModel>,
    private val onItemClickListener: (city: CityModel) -> Unit
) : RecyclerView.Adapter<CityListAdapter.CityListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListViewHolder {
        return CityListViewHolder(
            RowCityItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CityListViewHolder, position: Int) {
        holder.bind(cityList[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    class CityListViewHolder(
        private val cityListBinding: RowCityItemBinding
    ) : RecyclerView.ViewHolder(cityListBinding.root) {

        fun bind(
            city: CityModel,
            onItemClickListener: (city: CityModel) -> Unit
        ) {

            cityListBinding.apply {
                tvCityName.text = city.name
                clCity.setOnClickListener {
                    onItemClickListener.invoke(city)
                }
            }
        }

    }

}