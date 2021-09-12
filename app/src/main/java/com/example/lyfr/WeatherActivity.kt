package com.example.lyfr

import CurrentWeather
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URL

class WeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
//        val weatherURL = "https://api.openweathermap.org/data/2.5/weather?zip=" + 84119 + ",us&appid=17f7e1b88d2773dd429bd27a7d747611&units=imperial"
//        val result = URL(weatherURL).readText()
        val obj = Json.decodeFromString<CurrentWeather>("""{"coord":{"lon":-112.0011,"lat":40.6916},"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01n"}],"base":"stations","main":{"temp":71.82,"feels_like":71.08,"temp_min":66.34,"temp_max":74.55,"pressure":1019,"humidity":60},"visibility":10000,"wind":{"speed":1.01,"deg":287,"gust":4},"clouds":{"all":1},"dt":1631412855,"sys":{"type":2,"id":2005637,"country":"US","sunrise":1631365513,"sunset":1631411047},"timezone":-21600,"id":0,"name":"West Valley City","cod":200}""")

        val currentTemp = findViewById<TextView>(R.id.tvTemperature)
        val currentCity = findViewById<TextView>(R.id.tvCityName)
        val weatherIcon = findViewById<ImageView>(R.id.weatherIcon)
        val currentCondition = findViewById<TextView>(R.id.tvWeatherCondition)
        val currentHigh = findViewById<TextView>(R.id.tvHIGHtemp)
        val currentLow = findViewById<TextView>(R.id.tvLowtemp)
        val currentFeelsLike = findViewById<TextView>(R.id.tvFeelsLikeTemp)
        val currentHumidity = findViewById<TextView>(R.id.tvHumidityPercent)
        val currentWind = findViewById<TextView>(R.id.tvWindSpeed)

        Picasso.with(this).load("https://openweathermap.org/img/wn/" + obj.weather[0].icon + "@2x.png").into(weatherIcon);
        currentTemp.text = ("%.0f".format(obj.main["temp"]) + getText(R.string.tvDegrees))
        currentCity.text = obj.name
        currentCondition.text = obj.weather[0].main.uppercase()
        currentHigh.text = ("%.0f".format(obj.main["temp_max"]))
        currentLow.text = ("%.0f".format(obj.main["temp_min"]))
        currentFeelsLike.text = ("%.0f".format(obj.main["feels_like"]))
        currentHumidity.text = obj.main["humidity"].toString()
        currentWind.text = obj.wind["speed"].toString()
    }
}