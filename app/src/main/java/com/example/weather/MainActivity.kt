package com.example.weather

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.locale.LocaleHelper
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainPresenter: MainPresenter
    private lateinit var binding: ActivityMainBinding

    private var snackBar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainPresenter = MainPresenter()
        mainPresenter.attachView(this)

        binding.updateButton.setOnClickListener {
            clickUpdate()
        }

        binding.russianFederationFlag.setOnClickListener {
            val context = LocaleHelper().setLocale(this, "ru")
            val resources = context?.resources
            binding.updateButton.text = resources?.getString(R.string.button_update_text)
        }

        binding.usaFlag.setOnClickListener {
            val context = LocaleHelper().setLocale(this, "en")
            val resources = context?.resources
            binding.updateButton.text = resources?.getString(R.string.button_update_text)
        }

        binding.swipeUpdate.setOnRefreshListener {
            clickUpdate()
        }
    }

    override fun onResume() {
        super.onResume()
        mainPresenter.startView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.detachView()
    }

    private fun clickUpdate() {
        mainPresenter.updateData()
        if (binding.swipeUpdate.isRefreshing) {
            binding.swipeUpdate.isRefreshing = false
        }
    }
    fun updateView(temp: String?, place: String?, date: String?, icon: IconCode) {
        snackBarHide()
        binding.tempWeather.text = temp
        binding.placeWeather.text = place
        binding.dateUpdateWeather.text = date
        binding.tempWeather.setCompoundDrawablesWithIntrinsicBounds(
            0, 0, 0, getIconId(icon)
        )
    }

    private fun getIconId(icon: IconCode) : Int {
        return when (icon) {
            IconCode.SUN -> R.drawable.sun
            IconCode.RAIN -> R.drawable.rain
            IconCode.SNOW -> R.drawable.snow
            IconCode.MIST -> R.drawable.mist
            IconCode.CLOUD -> R.drawable.cloud
            IconCode.UNDEFINE -> 0
        }
    }

    fun getLocation() : Pair<String, String> {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var location: Location? = null
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                200
            )
        }

        val providers: List<String> = locationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val l: Location = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                // Found best last known location: %s", l);
                bestLocation = l
            }
        }
        val longitude = bestLocation?.longitude
        val latitude = bestLocation?.latitude

        Log.i("TEST_LOG", "${longitude} :: ${latitude}")
        return Pair(longitude.toString(), latitude.toString())
    }

    fun snackBarWithError(errorCode: ErrorCode) {
        val text: String = when (errorCode) {
            ErrorCode.INCORRECT_REQUEST -> getString(R.string.server_error)
            ErrorCode.REQUEST_TIMEOUT -> getString(R.string.request_timeout_error)
            ErrorCode.NOT_CONNECT -> getString(R.string.data_not_load)
            ErrorCode.SOME_ERROR -> getString(R.string.unknown_error)
            ErrorCode.UNKNOWN_LOCATION -> getString(R.string.unknown_location)
        }
        snackBar = Snackbar
            .make(binding.root, text, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry_on_error) {
                mainPresenter.updateData()
            }
        snackBar?.show()
    }

    fun snackBarHide() {
        snackBar?.dismiss()
    }
}