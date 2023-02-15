package com.example.weather

import com.example.weather.source.openweather.WeatherInfo

class MainPresenter: CallbackModel {
    private var view: MainActivity? = null
    private val model = MainModel(this)

    fun attachView(view: MainActivity) {
        this.view = view
        model.getData()
    }

    fun updateData() {

    }

    override fun successful(data: WeatherInfo) {
        view?.updateView(data.temp, data.place, data.date)
    }

    override fun failed(message: String?) {
        TODO("Not yet implemented")
    }
}