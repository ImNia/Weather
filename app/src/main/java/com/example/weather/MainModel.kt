package com.example.weather

import com.example.weather.source.openweather.OpenWeatherRequest
import com.example.weather.source.openweather.OpenWeatherSetting
import com.example.weather.source.openweather.WeatherInfo
import com.example.weather.source.openweather.model.OpenWeatherDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class MainModel(val callbackModel: CallbackModel) {
    //https://openweathermap.org/current
    private val weatherRequest: OpenWeatherRequest = OpenWeatherSetting.createRequest
    private var data: OpenWeatherDTO? = null

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
                    //TODO check what value exist
                    callbackModel.successful(handleData(data!!))
                } else {
                    println("Is not successful")
                }
            }
        })
    }


    private fun handleData(data: OpenWeatherDTO): WeatherInfo {
        return WeatherInfo(
            temp = tempKelvinToCelsius(data.main?.temp),
            place = data.name,
            date = data.timezone
        )
    }
    private fun tempKelvinToCelsius(temp: String?): String? {
        return temp?.let { (it.toDouble() - KALVIN_CONST).roundToInt().toString() }
    }

    companion object {
        const val KALVIN_CONST = 273.15
    }
}