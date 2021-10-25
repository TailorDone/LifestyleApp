package com.example.lyfr

import android.content.Context
import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import kotlin.math.pow

class WeatherActivity : AppCompatActivity() {
    var zip = ""
    val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as LYFR_Application).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val currentTemp = findViewById<TextView>(R.id.tvTemperature)
        val currentCity = findViewById<TextView>(R.id.tvCityName)
        val weatherIcon = findViewById<ImageView>(R.id.weatherIcon)
        val currentCondition = findViewById<TextView>(R.id.tvWeatherCondition)
        val currentHigh = findViewById<TextView>(R.id.tvHIGHtemp)
        val currentLow = findViewById<TextView>(R.id.tvLowtemp)
        val currentFeelsLike = findViewById<TextView>(R.id.tvFeelsLikeTemp)
        val currentHumidity = findViewById<TextView>(R.id.tvHumidityPercent)
        val currentWind = findViewById<TextView>(R.id.tvWindSpeed)

        userViewModel.user.observe(this, Observer { currentUser ->
            currentUser?.let {
                if (currentUser.profilePicturePath != null) {
                    loadImageFromStorage(currentUser.profilePicturePath.toString())
                }
                zip = currentUser.zip

                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val apiService = retrofit.create(MyApiEndpointInterface::class.java)

                val myZip = zip + ",us"
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
        })

        val profilePic = findViewById<ImageView>(R.id.profilePicture)
        profilePic.setOnClickListener{
            val editProfileIntent = Intent(this, NewUserActivity::class.java).apply {
            }
            startActivity(editProfileIntent)
        }
    }

    private fun loadImageFromStorage(path: String) : Bitmap? {
        try {
            val f = File(path)
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