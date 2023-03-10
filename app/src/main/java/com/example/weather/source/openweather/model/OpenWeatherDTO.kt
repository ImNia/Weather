package com.example.weather.source.openweather.model

data class OpenWeatherDTO(
    val coord: Coord?,
    val weather: List<Weather>?,
    val base: String?,
    val main: Main?,
    val visibility: String?,
    val wind: Wind?,
    val clouds: Clouds?,
    val dt: String?,
    val sys: Sys?,
    val timezone: String?,
    val id: String?,
    val name: String?,
    val cod: String?
)