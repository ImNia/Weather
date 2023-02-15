package com.example.weather

import com.example.weather.source.openweather.WeatherInfo

class MainPresenter: CallbackModel {
    private var view: MainActivity? = null
    private val model = MainModel(this)

    fun attachView(view: MainActivity) {
        this.view = view
    }
    fun detachView() {
        view = null
    }

    fun startView() {
        updateData()
    }

    fun updateData() {
        val locationData = view?.getLocation()
//        (longitude, latitude)
        if(locationData?.first != null || locationData?.second != null) {
            model.getData(locationData.first, locationData.second)
        }
    }

    override fun successful(data: WeatherInfo) {
        view?.updateView(data.temp, data.place, data.date, iconToCode(data.weatherToIcon))
    }

    private fun iconToCode(weather: String?) : IconCode {
        val id = weather?.toInt()
        if (id != null) {
            return when {
                id < 600 -> IconCode.RAIN
                id in 600..699 -> IconCode.SNOW
                id in 700..799 -> IconCode.MIST
                id == 800 -> IconCode.SUN
                id > 800 -> IconCode.CLOUD
                else -> IconCode.UNDEFINE
            }
        }
        return IconCode.UNDEFINE
    }

    override fun failed(errorCode: ErrorCode) {
        view?.snackBarWithError(errorCode)
    }
}