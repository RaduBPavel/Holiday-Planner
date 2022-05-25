package com.example.planner.ui.locations

import com.example.planner.ui.network.WeatherApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class Location(
    val name: String,
) {
    var temperature: Int = 0
    var airTemperature: Int = 0
    var humidity: Int = 0
    var isDay: Boolean = false

    fun updateValues(weatherApiResponse: WeatherApiResponse) {
        temperature = Integer.parseInt(weatherApiResponse.current["temp_c"].toString())
        airTemperature = Integer.parseInt(weatherApiResponse.current["feelslike_c"].toString())
        humidity = Integer.parseInt(weatherApiResponse.current["humidity"].toString())
        isDay = Integer.parseInt(weatherApiResponse.current["is_day"].toString()) == 1
    }
}