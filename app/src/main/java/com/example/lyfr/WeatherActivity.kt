package com.example.lyfr

import CurrentWeather
import MyApiEndpointInterface
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class WeatherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val profilePic = findViewById<ImageView>(R.id.profilePicture)
        profilePic.setOnClickListener{
            val editProfileIntent = Intent(this, NewUserActivity::class.java).apply {
            }
            startActivity(editProfileIntent)
        }

        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val savedZip = sharedPref.getString("zip", "")
        val profilePicture = sharedPref.getString("profilePicture", "")

        if (profilePicture != null) {
            loadImageFromStorage(profilePicture)
        }

        val currentTemp = findViewById<TextView>(R.id.tvTemperature)
        val currentCity = findViewById<TextView>(R.id.tvCityName)
        val weatherIcon = findViewById<ImageView>(R.id.weatherIcon)
        val currentCondition = findViewById<TextView>(R.id.tvWeatherCondition)
        val currentHigh = findViewById<TextView>(R.id.tvHIGHtemp)
        val currentLow = findViewById<TextView>(R.id.tvLowtemp)
        val currentFeelsLike = findViewById<TextView>(R.id.tvFeelsLikeTemp)
        val currentHumidity = findViewById<TextView>(R.id.tvHumidityPercent)
        val currentWind = findViewById<TextView>(R.id.tvWindSpeed)

        val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(MyApiEndpointInterface::class.java)

        val myZip = savedZip + ",us"
        val call = apiService.getWeather(myZip, "17f7e1b88d2773dd429bd27a7d747611", "imperial")
        call.enqueue( object : Callback<CurrentWeather> {
            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                val weatherIconNumber = response.body()?.weather?.get(0)?.icon
                Picasso.with(this@WeatherActivity).load("https://openweathermap.org/img/wn/" + weatherIconNumber + "@2x.png").into(weatherIcon);
                currentTemp.text = ("%.0f".format(response.body()?.main?.get("temp")) + getText(R.string.tvDegrees))
                currentCity.text = response.body()?.name ?: "Unknown"
                currentCondition.text = response.body()?.weather?.get(0)?.main?.uppercase() ?: "Conditions"
                currentHigh.text = ("%.0f".format(response.body()?.main?.get("temp_max")))
                currentLow.text = ("%.0f".format(response.body()?.main?.get("temp_min")))
                currentFeelsLike.text = ("%.0f".format(response.body()?.main?.get("feels_like")))
                currentHumidity.text = ("%.0f".format(response.body()?.main?.get("humidity")))
                currentWind.text = response.body()?.wind?.get("speed").toString()
            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                currentTemp.text = "--"
                currentCity.text = "Unknown"
                currentCondition.text = "Conditions"
                currentHigh.text = "--"
                currentLow.text = "--"
                currentFeelsLike.text = "--"
                currentHumidity.text = "--"
                currentWind.text = "--"
            }
        })
    }

    private fun loadImageFromStorage(path: String) {
        try {
            val f = File(path, "profile.jpg")
            val b =  BitmapFactory.decodeStream(FileInputStream(f))
            val img = findViewById<View>(R.id.profilePicture) as ImageView
            img.setImageBitmap(b)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
}