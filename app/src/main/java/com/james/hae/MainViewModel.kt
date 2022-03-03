package com.james.hae

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.james.hae.api.API
import com.james.hae.api.API.Scheme
import com.james.hae.api.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

	private val api = API(Scheme.HTTP, "weather.bfsah.com")

	private val possibleCities = listOf(
		"beijing",
		"berlin",
		"cardiff",
		"edinburgh",
		"london",
		"nottingham",
	)

	val cities = MutableStateFlow<List<Weather>>(listOf())

	init {
		viewModelScope.launch(Dispatchers.IO) {
			possibleCities.mapNotNull {
				try {
					api.getWeather(it)
				} catch (e: Exception) {
					e.printStackTrace()
					null
				}
			}.let { cities.emit(it) }
		}
	}

}