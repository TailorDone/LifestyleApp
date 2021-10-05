package com.example.lyfr
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel

class WeatherViewModel : ViewModel() {
    private lateinit var jsonData: MutableLiveData<WeatherData>

    val weatherData : MutableLiveData<WeatherData> by lazy {
        MutableLiveData<WeatherData>()
    }
//    var WeatherRepository mWeatherRepository;
//
//    fun WeatherViewModel(application: Application){
//        super(application)
//    }
//
//    fun getZip() : String {
//        return mZip;
//    }
//
//    fun setZip(zip : String){
//        mZip = zip;
//    }

}