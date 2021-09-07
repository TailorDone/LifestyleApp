package com.example.lyfr

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlin.math.pow

val POUNDS_TO_KILOGRAM = 0.454
val INCHES_TO_METERS = 0.0254

class BMIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmiactivity)

        val userHeight = findViewById<TextView>(R.id.tvHeightInches)
        val userHeightMeters = findViewById<TextView>(R.id.tvHeightMeters)

        val userWeight = findViewById<TextView>(R.id.tvWeightPounds)
        val userWeightKilos = findViewById<TextView>(R.id.tvWeightKilos)

        val userBMI = findViewById<TextView>(R.id.tvBMIValue)


        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val savedHeight = sharedPref.getString("height", "")
        val savedWeight = sharedPref.getString("weight", "")
        val kg = savedWeight?.toDouble()?.times(POUNDS_TO_KILOGRAM)
        val meters = savedHeight?.toDouble()?.times(INCHES_TO_METERS)
        val meters_squared = meters?.pow(2)
        val BMI = (meters_squared?.let { kg?.div(it) })

        userHeight.setText("%.0f".format(savedHeight?.toDouble()))
        userHeightMeters.setText("%.2f".format(meters))
        userWeight.setText("%.0f".format(savedWeight?.toDouble()))
        userWeightKilos.setText("%.2f".format(kg))
        userBMI.setText("%.1f".format(BMI))
    }
}