package com.james.hae

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.james.hae.api.Weather
import com.james.hae.databinding.SingleWeatherBinding

class WeatherAdapter : ListAdapter<Weather, WeatherAdapter.ViewHolder>(
	DIFF_CALLBACK
) {

	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Weather>() {
			override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean {
				return oldItem.city == newItem.city
			}

			override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean {
				return oldItem.hashCode() == newItem.hashCode()
			}
		}
	}

	class ViewHolder(
		private val binding: SingleWeatherBinding
	) : RecyclerView.ViewHolder(binding.root) {
		fun bind(weather: Weather) {
			binding.item = weather
			binding.executePendingBindings()
		}
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		return SingleWeatherBinding
			.inflate(layoutInflater, parent, false)
			.let { ViewHolder(it) }
	}

}