package com.example.planner.ui.locations

import com.example.planner.ui.network.RestClient
import com.example.planner.ui.network.WeatherApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class Location(
    val name: String,
    val subscribers: List<String>
) {
    var temperature: Double = 0.0
    var airTemperature: Double = 0.0
    var humidity: Int = 0
    var isDay: Boolean = false
    var windSpeed: Double = 0.0

    suspend fun updateValues(restClient: RestClient) {
        val weatherApiResponse = restClient.getData(name)
        temperature = weatherApiResponse!!.current["temp_c"].toString().toDouble()
        airTemperature = weatherApiResponse.current["feelslike_c"].toString().toDouble()
        humidity = Integer.parseInt(weatherApiResponse.current["humidity"].toString())
        isDay = Integer.parseInt(weatherApiResponse.current["is_day"].toString()) == 1
        windSpeed = weatherApiResponse.current["wind_kph"].toString().toDouble()
    }

}