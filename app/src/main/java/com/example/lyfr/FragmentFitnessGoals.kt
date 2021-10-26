package com.example.lyfr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlin.math.abs

class FragmentFitnessGoals : Fragment() {
    var BMR = 0
    var lifestyleScaleFactor = 0.0
    var lifestyle = 0
    var weightChangeGoal = 0.0
    var weightGoalOption = 0
    var name : String = ""
    var sex : String = ""
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((activity?.application as LYFR_Application).repository)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var fragmentView = inflater.inflate(R.layout.fragment_fitness_goals, container, false)

        val userBMR = fragmentView.findViewById<TextView>(R.id.tvBMRValue)
        val caloricGoal = fragmentView.findViewById<TextView>(R.id.tvCaloriesNeededValue)
        val seekBar = fragmentView.findViewById<SeekBar>(R.id.seekBarLifeStyle)
        var lifestyleTextView = fragmentView.findViewById<TextView>(R.id.tvLifeStyleValue)
        val loseWeightOption = fragmentView.findViewById<RadioButton>(R.id.rbLose)
        val maintainWeightOption = fragmentView.findViewById<RadioButton>(R.id.rbMaintain)
        val gainWeightOption = fragmentView.findViewById<RadioButton>(R.id.rbGain)
        val weightValueRow = fragmentView.findViewById<TableRow>(R.id.trWeightGoalAmount)
        val weightValueNumber = fragmentView.findViewById<EditText>(R.id.etWeightGoalNumber)
        val warning = fragmentView.findViewById<TextView>(R.id.tvWarning)
        val radioGroup = fragmentView.findViewById<RadioGroup>(R.id.rgWeightGoals)

        userViewModel.user.observe(viewLifecycleOwner, Observer { currentUser ->
            currentUser?.let {
                lifestyle = currentUser.lifestyle
                weightGoalOption = currentUser.weightGoalOption
                weightChangeGoal = currentUser.weightChangeGoal
                name = currentUser.name
                sex = currentUser.sex
                val weight = currentUser.weight
                val height = currentUser.height
                val age = currentUser.age
                val weightInKG = weight.times(POUNDS_TO_KILOGRAM)
                val heightInCM = height.times(INCHES_TO_CENTIMETERS)

                seekBar.progress = lifestyle
                lifestyleTextView.text = activityLevelDictionary[lifestyle]?.lifestyle
                lifestyleScaleFactor = activityLevelDictionary[lifestyle]?.scale!!

                val weightGoalID = weightGoalDictionary[weightGoalOption]
                val selected = weightGoalID?.let { it1 -> fragmentView.findViewById<RadioButton>(it1) }
                if (selected != null) {
                    selected.isChecked = true
                }
                if (selected == loseWeightOption || selected == gainWeightOption)
                    weightValueRow.visibility = View.VISIBLE

                weightValueNumber.setText(abs(weightChangeGoal).toString())

                BMR = if (sex == "M"){
                    ((10 * weightInKG) + (6.25 * heightInCM) - (5 * age) + 5).toInt()
                } else {
                    ((10 * weightInKG) + (6.25 * heightInCM) - (5 * age) - 161).toInt()
                }

                userBMR.text = BMR.toString()
                val  caloricGoalValue = calculateCaloricGoal()
                caloricGoal.text = caloricGoalValue.toString()

                if ((caloricGoalValue < 1200 && sex == "M") || (caloricGoalValue < 1000 && sex == "F"))
                    warning.text = "Warning: You are below the daily recommended caloric minimum."
            }
        })

        loseWeightOption.setOnClickListener {
            weightValueRow.visibility = View.VISIBLE
            weightGoalOption = 0
        }
        maintainWeightOption.setOnClickListener {
            weightValueRow.visibility = View.GONE
            weightGoalOption = 1
        }
        gainWeightOption.setOnClickListener {
            weightValueRow.visibility = View.VISIBLE
            weightGoalOption = 2
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                lifestyleTextView.text = activityLevelDictionary[progress]?.lifestyle ?: "Lifestyle"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //here we can write some code to do something whenever the user touche the seekbar
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                lifestyleScaleFactor = activityLevelDictionary[seekBar.progress]?.scale!!
                lifestyle = seekBar.progress
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
                    weightChangeGoal = weightValueNumber.text.toString().toDouble() * -1
                    warning.text = "Warning: Losing more than 2 pounds a week is ill-advised."
                    warning.visibility = View.VISIBLE
                }
                else if (weightGoal == gainWeightOption && weightValueNumber.text.toString().toDouble() > 2) {
                    weightChangeGoal = weightValueNumber.text.toString().toDouble()
                    warning.text = "Warning: Gaining more than 2 pounds a week is ill-advised."
                    warning.visibility = View.VISIBLE
                }
                else {
                    if (weightGoal == maintainWeightOption)
                        weightChangeGoal = 0.0
                    else {
                        weightChangeGoal = weightValueNumber.text.toString().toDouble()
                        if (weightGoal == loseWeightOption)
                            weightChangeGoal *= -1
                    }
                }
                userViewModel.updateFitnessGoals(lifestyle, weightChangeGoal, weightGoalOption, name)
            }
        }
        return fragmentView
    }

    fun calculateCaloricGoal(): Int {
        var caloricChange = (weightChangeGoal * POUNDS_TO_CALORIES) / 7
        var caloricGoal = (BMR * lifestyleScaleFactor).toInt()
        caloricGoal = (caloricGoal + caloricChange).toInt()
        return caloricGoal
    }
}