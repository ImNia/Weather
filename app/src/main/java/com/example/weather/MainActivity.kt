package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.source.openweather.OpenWeather

class MainActivity : AppCompatActivity() {
    private lateinit var mainPresenter: MainPresenter
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO save state
        mainPresenter = MainPresenter()
        mainPresenter.attachView(this)

        binding.updateButton.setOnClickListener {

        }
    }

    fun updateView(temp: String?, place: String?, date: String?) {
        binding.tempWeather.text = temp
        binding.placeWeather.text = place
        binding.dateUpdateWeather.text = date
        println("$temp, $place, $date")
    }
}