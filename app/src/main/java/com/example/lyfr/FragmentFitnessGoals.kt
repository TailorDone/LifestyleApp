package com.example.lyfr

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlin.math.abs




class FragmentFitnessGoals : Fragment() {
    var BMR = 0
    var lifestyleScaleFactor = 0.0
    var weightChange = 0.0

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var fragmentView = inflater.inflate(R.layout.fragment_fitness_goals, container, false)

        val userBMR = fragmentView.findViewById<TextView>(R.id.tvBMRValue)
        val caloricGoal = fragmentView.findViewById<TextView>(R.id.tvCaloriesNeededValue)

        val sharedPref = this.requireActivity()
            .getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val savedHeight = arguments?.getString("height")?.toDouble()
        val savedWeight = arguments?.getString("weight")?.toDouble()
        val savedAge = arguments?.getString("age")?.toInt()
        val savedSex = arguments?.getString("sex")
        val weightInKG = savedWeight?.times(POUNDS_TO_KILOGRAM)
        val heightInCM = savedHeight?.times(INCHES_TO_CENTIMETERS)

        val seekBar = fragmentView.findViewById<SeekBar>(R.id.seekBarLifeStyle)
        var lifestyle = fragmentView.findViewById<TextView>(R.id.tvLifeStyleValue)
        val savedLifestyle = sharedPref.getString("lifestyle", "0")?.toInt()
        if (savedLifestyle != null) {
            seekBar.progress = savedLifestyle
            lifestyle.text = activityLevelDictionary[savedLifestyle]?.lifestyle ?: "Sedentary"
            lifestyleScaleFactor = activityLevelDictionary[savedLifestyle]?.scale ?: 1.2
        }

        val loseWeightOption = fragmentView.findViewById<RadioButton>(R.id.rbLose)
        val maintainWeightOption = fragmentView.findViewById<RadioButton>(R.id.rbMaintain)
        val gainWeightOption = fragmentView.findViewById<RadioButton>(R.id.rbGain)
        val weightValueRow = fragmentView.findViewById<TableRow>(R.id.trWeightGoalAmount)
        val weightValueNumber = fragmentView.findViewById<EditText>(R.id.etWeightGoalNumber)
        val warning = fragmentView.findViewById<TextView>(R.id.tvWarning)
        val radioGroup = fragmentView.findViewById<RadioGroup>(R.id.rgWeightGoals)

        val savedWeightGoal = sharedPref.getString("weightGoal", maintainWeightOption.id.toString())
        if (savedWeightGoal != null) {
            val selected = fragmentView.findViewById<RadioButton>(savedWeightGoal.toInt())
            selected.isChecked = true
            if (selected == loseWeightOption || selected == gainWeightOption)
                weightValueRow.visibility = View.VISIBLE
        }

        val savedWeightChange = sharedPref.getString("weightChange", "0")
        if (savedWeightChange != null) {
            weightValueNumber.setText(abs(savedWeightChange.toDouble()).toString())
            weightChange = savedWeightChange.toDouble()
        }

        if (savedSex == "M"){
            BMR = ((10 * weightInKG!!) + (6.25 * heightInCM!!) - (5 * savedAge!!) + 5).toInt()
        } else {
            BMR = ((10 * weightInKG!!) + (6.25 * heightInCM!!) - (5 * savedAge!!) - 161).toInt()
        }
        userBMR.text = BMR.toString()
        caloricGoal.text = calculateCaloricGoal().toString()

        loseWeightOption.setOnClickListener {
            weightValueRow.visibility = View.VISIBLE
        }
        gainWeightOption.setOnClickListener {
            weightValueRow.visibility = View.VISIBLE
        }
        maintainWeightOption.setOnClickListener {
            weightValueRow.visibility = View.GONE
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                lifestyle.text = activityLevelDictionary[progress]?.lifestyle ?: "Lifestyle"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //here we can write some code to do something whenever the user touche the seekbar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                lifestyleScaleFactor = activityLevelDictionary[seekBar.progress]?.scale!!
            }
        })

        val calculateButton = fragmentView.findViewById<Button>(R.id.buttonCalculate)
        calculateButton.setOnClickListener{
            val selectedOption = radioGroup.checkedRadioButtonId
            val weightGoal = fragmentView.findViewById<RadioButton>(selectedOption)

            if (weightGoal == loseWeightOption && weightValueNumber.text.toString().isBlank())
                Toast.makeText(this.requireActivity(), "Enter how many pounds you would like to lose.", Toast.LENGTH_SHORT).show()
            else if (weightGoal == gainWeightOption && weightValueNumber.text.toString().isBlank())
                Toast.makeText(this.requireActivity(), "Enter how many pounds you would like to gain.", Toast.LENGTH_SHORT).show()
            else {
                if (weightGoal == loseWeightOption && weightValueNumber.text.toString().toDouble() > 2) {
                    weightChange = weightValueNumber.text.toString().toDouble() * -1
                    warning.text = "Warning: Losing more than 2 pounds a week is ill-advised."
                    warning.visibility = View.VISIBLE
                }
                else if (weightGoal == gainWeightOption && weightValueNumber.text.toString().toDouble() > 2) {
                    weightChange = weightValueNumber.text.toString().toDouble()
                    warning.text = "Warning: Gaining more than 2 pounds a week is ill-advised."
                    warning.visibility = View.VISIBLE
                }
                else {
                    if (weightGoal == maintainWeightOption)
                        weightChange = 0.0
                    else {
                        weightChange = weightValueNumber.text.toString().toDouble()
                        if (weightGoal == loseWeightOption)
                            weightChange *= -1
                    }
                }
                val caloricGoalValue = calculateCaloricGoal()
                caloricGoal.text = caloricGoalValue.toString()
                with (sharedPref.edit()) {
                    putString("weightGoal", selectedOption.toString())
                    putString("weightChange", weightChange.toString())
                    putString("lifestyle", seekBar.progress.toString())
                    commit()
                }
                if ((caloricGoalValue < 1200 && savedSex == "M") || (caloricGoalValue < 1000 && savedSex == "F"))
                    warning.text = "Warning: You are below the daily recommended caloric minimum."
            }
        }
        return fragmentView
    }

    fun calculateCaloricGoal(): Int {
        var caloricChange = (weightChange * POUNDS_TO_CALORIES) / 7
        var caloricGoal = (BMR * lifestyleScaleFactor).toInt()
        caloricGoal = (caloricGoal + caloricChange).toInt()
        return caloricGoal
    }
}