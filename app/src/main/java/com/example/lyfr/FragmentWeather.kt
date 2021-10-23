package com.example.lyfr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentWeather : Fragment() {
    val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    var zip = ""
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((activity?.application as LYFR_Application).repository)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var fragmentView = inflater.inflate(R.layout.fragment_weather, container, false)

        val currentTemp = fragmentView.findViewById<TextView>(R.id.tvTemperature)
        val currentCity = fragmentView.findViewById<TextView>(R.id.tvCityName)
        val weatherIcon = fragmentView.findViewById<ImageView>(R.id.weatherIcon)
        val currentCondition = fragmentView.findViewById<TextView>(R.id.tvWeatherCondition)
        val currentHigh = fragmentView.findViewById<TextView>(R.id.tvHIGHtemp)
        val currentLow = fragmentView.findViewById<TextView>(R.id.tvLowtemp)
        val currentFeelsLike = fragmentView.findViewById<TextView>(R.id.tvFeelsLikeTemp)
        val currentHumidity = fragmentView.findViewById<TextView>(R.id.tvHumidityPercent)
        val currentWind = fragmentView.findViewById<TextView>(R.id.tvWindSpeed)

        userViewModel.user.observe(viewLifecycleOwner, Observer { currentUser ->
            currentUser?.let {
                zip = currentUser.zip
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val apiService = retrofit.create(MyApiEndpointInterface::class.java)

                val zipParameter =  zip + ",us"
                val call = apiService.getWeather(zipParameter, "17f7e1b88d2773dd429bd27a7d747611", "imperial")
                call.enqueue( object : Callback<CurrentWeather> {
                    override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                        val weatherIconNumber = response.body()?.weather?.get(0)?.icon
                        Picasso.with(this@FragmentWeather.context).load("https://openweathermap.org/img/wn/" + weatherIconNumber + "@2x.png").into(weatherIcon);
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

        return fragmentView
    }
}