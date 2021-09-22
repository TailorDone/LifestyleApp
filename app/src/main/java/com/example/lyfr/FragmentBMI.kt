package com.example.lyfr

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.math.pow

class FragmentBMI : Fragment(R.layout.activity_bmiactivity) {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var fragmentView = inflater.inflate(R.layout.fragment_bmiactivity, container, false)

        val userHeight = fragmentView.findViewById<TextView>(R.id.tvHeightInches)
        val userHeightMeters = fragmentView.findViewById<TextView>(R.id.tvHeightMeters)
        val userWeight = fragmentView.findViewById<TextView>(R.id.tvWeightPounds)
        val userWeightKilos = fragmentView.findViewById<TextView>(R.id.tvWeightKilos)
        val userBMI = fragmentView.findViewById<TextView>(R.id.tvBMIValue)

        val height = arguments?.getString("height")?.toDouble()
        val weight = arguments?.getString("weight")?.toDouble()
        val kg = weight?.times(POUNDS_TO_KILOGRAM)
        val meters = height?.times(INCHES_TO_METERS)
        val meters_squared = meters?.pow(2)
        val BMI = (meters_squared?.let { kg?.div(it) })

        userHeight.setText("%.0f".format(height))
        userHeightMeters.setText("%.2f".format(meters))
        userWeight.setText("%.0f".format(weight))
        userWeightKilos.setText("%.2f".format(kg))
        userBMI.setText("%.1f".format(BMI))

        return fragmentView
    }
}