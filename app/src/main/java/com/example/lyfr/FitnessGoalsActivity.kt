package com.example.lyfr

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FitnessGoalsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitness_goals)

        val userHeight = findViewById<TextView>(R.id.tvHeightInches)
        val userHeightMeters = findViewById<TextView>(R.id.tvHeightMeters)

        val userWeight = findViewById<TextView>(R.id.tvWeightPounds)
        val userWeightKilos = findViewById<TextView>(R.id.tvWeightKilos)

        val userBMI = findViewById<TextView>(R.id.tvBMRValue)


        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val savedHeight = sharedPref.getString("height", "")
        val savedWeight = sharedPref.getString("weight", "")
        val savedAge = sharedPref.getString("age", "")
        val savedSex = sharedPref.getString("sex", "M/F")
        val weightInKG = savedWeight?.toDouble()?.times(POUNDS_TO_KILOGRAM)
        val meters = savedHeight?.toDouble()?.times(INCHES_TO_METERS)
        val heightInCM = savedHeight?.toDouble()?.times(2.54)
        val age = savedAge?.toInt()
        var BMR = 0.0
        if (savedSex == "M"){
            BMR = (10 * weightInKG!!) + (6.25 * heightInCM!!) - (5 * age!!) + 5
        } else {
            BMR = (10 * weightInKG!!) + (6.25 * heightInCM!!) - (5 * age!!) - 161
        }

        userHeight.setText("%.0f".format(savedHeight?.toDouble()))
        userHeightMeters.setText("%.2f".format(meters))
        userWeight.setText("%.0f".format(savedWeight?.toDouble()))
        userWeightKilos.setText("%.2f".format(weightInKG))
        userBMI.setText("%.0f".format(BMR))
    }
}