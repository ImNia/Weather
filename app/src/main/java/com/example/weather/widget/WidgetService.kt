package com.example.weather.widget

import android.R
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import com.example.weather.*
import com.example.weather.databinding.WeatherWidgetBinding
import com.example.weather.source.openweather.WeatherInfo
import java.util.*


class WidgetService: CallbackModel, Service() {
    private val model = MainModel(this)

    override fun onCreate() {
        super.onCreate()
        updateData()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // generates random number
        val random = Random()
        val randomInt: Int = random.nextInt(100)
        val lastUpdate = "R: $randomInt"
        // Reaches the view on widget and displays the number
//        val view = RemoteViews(packageName, WeatherWidgetBinding.inflate())
//        view.setTextViewText(R.id.temp_appwidget_text, lastUpdate)
        val theWidget = ComponentName(this, WeatherWidget::class.java)
        val manager = AppWidgetManager.getInstance(this)
//        manager.updateAppWidget(theWidget, view)
        println(lastUpdate)
        return super.onStartCommand(intent, flags, startId)
    }

    fun updateData() {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
           println("HAVE A PROBLEM")
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

        Log.i("TEST_LOG", "IN service :: ${longitude} :: ${latitude}")
    }

    override fun successful(data: WeatherInfo) {
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
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}