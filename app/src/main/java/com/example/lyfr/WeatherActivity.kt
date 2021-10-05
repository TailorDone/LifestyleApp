package com.example.lyfr

import CurrentWeather
import MyApiEndpointInterface
import android.content.Context
import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var mCurrentTemp : TextView
    private lateinit var mCurrentCity : TextView
    private lateinit var mCurrentCondition : TextView
    private lateinit var mCurrentHigh : TextView
    private lateinit var mCurrentLow : TextView
    private lateinit var mCurrentFeelsLike : TextView
    private lateinit var mCurrentHumidity : TextView
    private lateinit var mCurrentWind : TextView
    private lateinit var mWeatherViewModel : WeatherViewModel
    private lateinit var mViewModelFactory: WeatherViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val profilePic = findViewById<ImageView>(R.id.profilePicture)
        profilePic.setOnClickListener{
            val editProfileIntent = Intent(this, NewUserActivity::class.java).apply {
            }
            startActivity(editProfileIntent)
        }

        //TO DO change this to not use shared preferences
        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val savedZip = sharedPref.getString("zip", "")
        val profilePicture = sharedPref.getString("profilePicture", "")
        val weatherIcon = findViewById<ImageView>(R.id.weatherIcon)

        if (profilePicture != null) {
            loadImageFromStorage(profilePicture)
        }

        //Get all the text views
        mCurrentTemp = findViewById<TextView>(R.id.tvTemperature)
        mCurrentCity = findViewById<TextView>(R.id.tvCityName)
        mCurrentCondition = findViewById<TextView>(R.id.tvWeatherCondition)
        mCurrentHigh = findViewById<TextView>(R.id.tvHIGHtemp)
        mCurrentLow = findViewById<TextView>(R.id.tvLowtemp)
        mCurrentFeelsLike = findViewById<TextView>(R.id.tvFeelsLikeTemp)
        mCurrentHumidity = findViewById<TextView>(R.id.tvHumidityPercent)
        mCurrentWind = findViewById<TextView>(R.id.tvWindSpeed)

        //Grab an instance of the view model
        mViewModelFactory = WeatherViewModelFactory()
        mWeatherViewModel = ViewModelProvider(this, mViewModelFactory).get(WeatherViewModel::class.java)

        //Set the observer
        val weatherDataObserver

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

    private fun loadImageFromStorage(path: String) : Bitmap? {
        try {
            val f = File(path, "profile.jpg")
            var b =  BitmapFactory.decodeStream(FileInputStream(f))
            val img = findViewById<View>(R.id.profilePicture) as ImageView
            b = getCircledBitmap(b)
            img.setImageBitmap(b)
            return b
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    fun getCircledBitmap(bitmap: Bitmap): Bitmap? {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.setAntiAlias(true)
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle((bitmap.width / 2).toFloat(),
            (bitmap.height / 2).toFloat(), (bitmap.width / 2).toFloat(), paint)
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }
}