package com.example.weather.source.openweather

import com.example.weather.source.openweather.model.OpenWeatherDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenWeather {
    //https://openweathermap.org/current
    private val weatherRequest: OpenWeatherRequest = OpenWeatherSetting.createRequest
    private var data : OpenWeatherDTO? = null


}