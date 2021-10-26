package com.example.lyfr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlin.math.pow

class FragmentBMI : Fragment(R.layout.activity_bmiactivity) {
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((activity?.application as LYFR_Application).repository)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var fragmentView = inflater.inflate(R.layout.fragment_bmiactivity, container, false)

        val userHeight = fragmentView.findViewById<TextView>(R.id.tvHeightInches)
        val userHeightMeters = fragmentView.findViewById<TextView>(R.id.tvHeightMeters)
        val userWeight = fragmentView.findViewById<TextView>(R.id.tvWeightPounds)
        val userWeightKilos = fragmentView.findViewById<TextView>(R.id.tvWeightKilos)
        val userBMI = fragmentView.findViewById<TextView>(R.id.tvBMIValue)

        userViewModel.user.observe(viewLifecycleOwner, Observer { currentUser ->
            currentUser?.let {
                val weight = currentUser.weight
                val height = currentUser.height
                val kg = weight.times(POUNDS_TO_KILOGRAM)
                val meters = height.times(INCHES_TO_METERS)
                val meters_squared = meters.pow(2)
                val BMI = (meters_squared.let { kg.div(it) })

                userHeight.text = "%.0f".format(height)
                userHeightMeters.text = ("%.2f".format(meters))
                userWeight.text =("%.0f".format(weight))
                userWeightKilos.text = ("%.2f".format(kg))
                userBMI.text = ("%.1f".format(BMI))
            }
        })

        return fragmentView
    }
}