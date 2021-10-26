package com.example.lyfr

import kotlinx.serialization.Serializable

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
) {}