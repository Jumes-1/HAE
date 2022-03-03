package com.james.hae.api

import android.util.JsonReader
import android.util.JsonWriter
import android.util.Log
import org.json.JSONObject
import org.json.JSONStringer
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class API(
	private val scheme: Scheme,
	private val host: String,
) {

	enum class Scheme(
		val actual: String,
	) {
		HTTP("http"),
		HTTPS("https"),
	}

	private val endpoint: String
		get() = "${scheme.actual}://$host"

	suspend fun get(path: String) = suspendCoroutine<String> { cont ->
		runCatching {
			try {
				val url = URL("$endpoint/$path")
				val connection = url.openConnection() as HttpURLConnection
				connection.connect()
				val input = connection.inputStream
				val response = String(input.readBytes())
				cont.resume(response)
			} catch (e: Exception) {
				e.printStackTrace()
				cont.resumeWith(Result.failure(e))
			}
		}
	}

	suspend fun getWeather(city: String): Weather {
		return get(city)
			.let { JSONObject(it) }
			.let {
				Weather(
					it.getString("city"),
					it.getString("country"),
					it.getInt("temperature"),
					it.getString("description"),
				)
			}
	}

}

data class Weather(
	val city: String,
	val country: String,
	val temperature: Int,
	val description: String,
) {
	val temperatureAsString: String
		get() = "$temperature°C"
}