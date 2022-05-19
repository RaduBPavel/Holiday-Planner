package com.example.planner.ui.locations

import kotlin.properties.Delegates

data class Location(
    val name: String,
) {
    var temperature: Int = 0
    var airTemperature: Int = 0
    var humidity: Int = 0
    var isDay: Boolean = false
    var condition: String = "Clear"

    fun updateValues() {

    }
}