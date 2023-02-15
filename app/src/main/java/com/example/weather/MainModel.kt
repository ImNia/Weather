package com.example.weather

import com.example.weather.source.openweather.OpenWeatherRequest
import com.example.weather.source.openweather.OpenWeatherSetting
import com.example.weather.source.openweather.WeatherInfo
import com.example.weather.source.openweather.model.OpenWeatherDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Calendar
import kotlin.math.roundToInt

class MainModel(val callbackModel: CallbackModel) {
    //https://openweathermap.org/current
    private val weatherRequest: OpenWeatherRequest = OpenWeatherSetting.createRequest
    private var data: OpenWeatherDTO? = null

    fun getData(longitude: String, latitude: String) {
        weatherRequest.getWeather(latitude, longitude, "1537e17da89b2fe3c52106b4654e00b3").enqueue(object : Callback<OpenWeatherDTO> {
            override fun onFailure(call: Call<OpenWeatherDTO>, t: Throwable) {
                when(t) {
                    is SocketTimeoutException -> {
                        callbackModel.failed(ErrorCode.REQUEST_TIMEOUT)
                    }
                    is UnknownHostException -> {
                        callbackModel.failed(ErrorCode.NOT_CONNECT)
                    }
                    else -> {
                        callbackModel.failed(ErrorCode.SOME_ERROR)
                    }
                }
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<OpenWeatherDTO>,
                response: Response<OpenWeatherDTO>
            ) {
                println(response.raw())
                if (response.isSuccessful) {
                    data = response.body() as OpenWeatherDTO
                    //TODO check what value exist
                    callbackModel.successful(handleData(data!!))
                } else {
                    callbackModel.failed(ErrorCode.INCORRECT_REQUEST)
                }
            }
        })
    }


    private fun handleData(data: OpenWeatherDTO): WeatherInfo {
        return WeatherInfo(
            temp = tempKelvinToCelsius(data.main?.temp),
            place = data.name,
            date = Calendar.getInstance().time.toString()
        )
    }
    private fun tempKelvinToCelsius(temp: String?): String? {
        return temp?.let { (it.toDouble() - KALVIN_CONST).roundToInt().toString() }
    }

    companion object {
        const val KALVIN_CONST = 273.15
    }
}