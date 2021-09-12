package com.example.lyfr

import CurrentWeather
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class WeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val currentTemp = findViewById<TextView>(R.id.tvTemperature)

        val obj = Json.decodeFromString<CurrentWeather>("""{"coord":{"lon":-112.0011,"lat":40.6916},"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01n"}],"base":"stations","main":{"temp":71.42,"feels_like":71.08,"temp_min":66.34,"temp_max":74.55,"pressure":1019,"humidity":60},"visibility":10000,"wind":{"speed":1.01,"deg":287,"gust":4},"clouds":{"all":1},"dt":1631412855,"sys":{"type":2,"id":2005637,"country":"US","sunrise":1631365513,"sunset":1631411047},"timezone":-21600,"id":0,"name":"West Valley City","cod":200}""")

        currentTemp.text = obj.weather[0].id.toString()
    }
}