package com.example.lyfr

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlin.math.pow
import kotlin.math.roundToLong

val POUNDS_TO_KILOGRAM = 0.454
val INCHES_TO_METERS = 0.0254

class BMIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmiactivity)

        val userAge = findViewById<TextView>(R.id.tvBMIAgeValue)
        val userSex = findViewById<TextView>(R.id.tvBMISexValue)
        val userHeight = findViewById<TextView>(R.id.tvBMIHeightValue)
        val userWeight = findViewById<TextView>(R.id.tvBMIWeightValue)
        val userBMI = findViewById<TextView>(R.id.tvBMIValue)


        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val savedAge = sharedPref.getString("age", "")
        val savedSex = sharedPref.getString("sex", "")
        val savedHeight = sharedPref.getString("height", "")
        val savedWeight = sharedPref.getString("weight", "")
        val kg = savedWeight?.toInt()?.times(POUNDS_TO_KILOGRAM)
        val meters = savedHeight?.toInt()?.times(INCHES_TO_METERS)
        val meters_squared = meters?.pow(2)
        val BMI = (meters_squared?.let { kg?.div(it) })

        userAge.setText(savedAge)
        userSex.setText(savedSex)
        userHeight.setText(savedHeight)
        userWeight.setText(savedWeight)
        userBMI.setText("%.1f".format(BMI))
    }
}