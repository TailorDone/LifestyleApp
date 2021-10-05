package com.example.lyfr

class WeatherData {
    private lateinit var mCurrentCondition: CurrentCondition

    class CurrentCondition{
        private var currentTemp : String = ""
        private var currentCity : String = ""
        private var currentCondition : String = ""
        private var currentHigh : String = ""
        private var currentLow : String = ""
        private var currentFeelsLike : String = ""
        private var currentHumidity : String = ""
        private var currentWind : String = ""
    }

    //Setters and Getters
    var currentCondition : CurrentCondition = mCurrentCondition
}