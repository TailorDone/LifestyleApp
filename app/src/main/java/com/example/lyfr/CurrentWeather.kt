package com.example.lyfr

import kotlinx.serialization.Serializable

//{"coord":{"lon":-0.1257,"lat":51.5085},
//    "weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04n"}],
//    "base":"stations",
//    "main":{"temp":289.05,"feels_like":288.52,"temp_min":285.22,"temp_max":291.07,"pressure":1019,"humidity":70},
//    "visibility":10000,
//    "wind":{"speed":0.45,"deg":279,"gust":2.24},
//    "clouds":{"all":90},
//    "dt":1631396700,
//    "sys":{"type":2,"id":2019646,"country":"GB","sunrise":1631338156,"sunset":1631384717},
//    "timezone":3600,
//    "id":2643743,
//    "name":"London",
//    "cod":200}

@Serializable
data class CurrentWeather(val coord: Map<String, Double>,
                          val weather: List<Weather>,
                          val base: String,
                          val main: Map<String, Double>,
                          val visibility: Int,
                          val wind: Map<String, Double>,
                          val clouds: Map<String, Int>,
                          val dt: Double,
                          val sys: Sys,
                          val timezone: Int,
                          val id: Int,
                          val name: String,
                          val cod: Int,
) {

}