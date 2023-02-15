package com.example.weather.source.openweather

import com.example.weather.source.openweather.model.OpenWeatherDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenWeather {
    //https://openweathermap.org/current
    private val weatherRequest: OpenWeatherRequest = OpenWeatherSetting.createRequest
    private var data : OpenWeatherDTO? = null

    fun getData() {
        weatherRequest.getWeather().enqueue(object : Callback<OpenWeatherDTO> {
            override fun onFailure(call: Call<OpenWeatherDTO>, t: Throwable) {
                t.printStackTrace()
                //TODO Add exception
                println("In error")
            }

            override fun onResponse(
                call: Call<OpenWeatherDTO>,
                response: Response<OpenWeatherDTO>
            ) {
                println(response.raw())
                if (response.isSuccessful) {
                    data = response.body() as OpenWeatherDTO
                    println(data)
                } else {
                    println("Is not successful")
                }
            }
        })

        println(data)
    }
}