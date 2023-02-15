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
        view?.showProgressBar()
        val locationData = view?.getLocation()
//        (longitude, latitude)
        if(locationData?.first != null || locationData?.second != null) {
            model.getData(locationData.first, locationData.second)
        }
    }

    override fun successful(data: WeatherInfo) {
        view?.hideProgressBar()
        view?.updateView(data.temp, data.place, data.date)
    }

    override fun failed(errorCode: ErrorCode) {
        view?.snackBarWithError(errorCode)
    }
}