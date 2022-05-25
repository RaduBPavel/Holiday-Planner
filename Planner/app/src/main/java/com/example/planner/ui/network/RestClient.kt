package com.example.planner.ui.network

import android.util.Log
import com.beust.klaxon.Klaxon
import okhttp3.*
import java.lang.Exception
import java.net.URL

private const val apiKey = "be079e43351a4eb1b58213718221704"
private const val baseUrl = "http://api.weatherapi.com/v1"

object RestClient {
    var client = OkHttpClient()

    suspend fun getData(cityName: String): WeatherApiResponse? {
        val url = URL(baseUrl)

        val request = Request.Builder()
            .addHeader("key", apiKey)
            .url("$url/current.json?key=$apiKey&q=$cityName&aqi=no")
            .build()

        try {
            val response = client.newCall(request).execute()
            val weatherApiResponse = Klaxon().parse<WeatherApiResponse>(response.body()!!.string())

            Log.i("REST", "call made")
            return weatherApiResponse
        } catch (e: Exception) {
            Log.i("REST", e.message!!)
        }

        return null
    }
}