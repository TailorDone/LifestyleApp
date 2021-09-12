package com.example.lyfr

import Weather
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

        val obj = Json.decodeFromString<Weather>("""{"a":42, "b": "str"}""")

        currentTemp.text = obj.b

    }
}