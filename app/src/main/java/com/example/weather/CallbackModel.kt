package com.example.weather

import com.example.weather.source.openweather.WeatherInfo

interface CallbackModel {
    fun successful(data: WeatherInfo)
    fun failed(message: String?)
}