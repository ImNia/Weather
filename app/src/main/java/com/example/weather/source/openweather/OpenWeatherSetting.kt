package com.example.weather.source.openweather

import com.example.weather.source.openweather.model.OpenWeatherDTO
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object OpenWeatherSetting  {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/weather/"
    val createRequest: OpenWeatherRequest
        get() = RetrofitClient.getClient(BASE_URL).create(OpenWeatherRequest::class.java)
}

object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        if(retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}

interface OpenWeatherRequest{
    @GET("?")
    fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String
    ): Call<OpenWeatherDTO>
}