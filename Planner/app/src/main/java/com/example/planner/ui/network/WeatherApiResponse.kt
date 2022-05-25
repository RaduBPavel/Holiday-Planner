package com.example.planner.ui.network

data class WeatherApiResponse(
    val location: Map<String, Any>,
    val current: Map<String, Any>) {
}