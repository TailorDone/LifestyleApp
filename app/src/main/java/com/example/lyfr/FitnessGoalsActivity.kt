package com.example.lyfr

import android.content.Context
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FitnessGoalsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitness_goals)

        val userBMI = findViewById<TextView>(R.id.tvBMRValue)

        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val savedHeight = sharedPref.getString("height", "")
        val savedWeight = sharedPref.getString("weight", "")
        val savedAge = sharedPref.getString("age", "")
        val savedSex = sharedPref.getString("sex", "M/F")
        val weightInKG = savedWeight?.toDouble()?.times(POUNDS_TO_KILOGRAM)
        val heightInCM = savedHeight?.toDouble()?.times(2.54)
        val age = savedAge?.toInt()
        var BMR = 0.0
        if (savedSex == "M"){
            BMR = (10 * weightInKG!!) + (6.25 * heightInCM!!) - (5 * age!!) + 5
        } else {
            BMR = (10 * weightInKG!!) + (6.25 * heightInCM!!) - (5 * age!!) - 161
        }
        userBMI.setText("%.0f".format(BMR))

        val loseWeightOption = findViewById<TextView>(R.id.rbLose)
        val maintainWeightOption = findViewById<TextView>(R.id.rbMaintain)
        val gainWeightOption = findViewById<TextView>(R.id.rbGain)
        val weightValueRow = findViewById<TableRow>(R.id.trWeightGoalAmount)

        loseWeightOption.setOnClickListener {
            weightValueRow.visibility = VISIBLE
        }
        gainWeightOption.setOnClickListener {
            weightValueRow.visibility = VISIBLE
        }
        maintainWeightOption.setOnClickListener {
            weightValueRow.visibility = GONE
        }

    }
}