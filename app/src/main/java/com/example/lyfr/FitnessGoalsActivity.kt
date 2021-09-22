package com.example.lyfr

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs
import com.example.lyfr.databinding.ActivityFitnessGoalsBinding


const val INCHES_TO_CENTIMETERS = 2.54
const val POUNDS_TO_CALORIES = 3500
val activityLevelDictionary = mapOf(0 to FitnessGoalsActivity.ActivityLevel("Sedentary", 1.2),
    1 to FitnessGoalsActivity.ActivityLevel("Lightly active", 1.375),
    2 to FitnessGoalsActivity.ActivityLevel("Moderately active", 1.55),
    3 to FitnessGoalsActivity.ActivityLevel("Very active", 1.725),
    4 to FitnessGoalsActivity.ActivityLevel("Extra active", 1.9))

class FitnessGoalsActivity : AppCompatActivity() {
    var BMR = 0
    var lifestyleScaleFactor = 0.0
    var weightChange = 0.0
    class ActivityLevel(val lifestyle: String, val scale : Double) {
    }

    private lateinit var binding: ActivityFitnessGoalsBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFitnessGoalsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_fitness_goals)



        val previewImage by lazy { findViewById<ImageButton>(R.id.image_preview) }
        previewImage.setImageURI(null)
        previewImage.setImageURI(ImageUri.latestTmpUri)

        previewImage.setOnClickListener{
            val editProfileIntent = Intent(this, NewUserActivity::class.java).apply {
            }
            startActivity(editProfileIntent)}


        val userBMR = findViewById<TextView>(R.id.tvBMRValue)
        val caloricGoal = findViewById<TextView>(R.id.tvCaloriesNeededValue)

        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)

        val savedHeight = sharedPref.getString("height", "")
        val savedWeight = sharedPref.getString("weight", "")
        val savedAge = sharedPref.getString("age", "")
        val savedSex = sharedPref.getString("sex", "M")
        val weightInKG = savedWeight?.toDouble()?.times(POUNDS_TO_KILOGRAM)
        val heightInCM = savedHeight?.toDouble()?.times(INCHES_TO_CENTIMETERS)
        val age = savedAge?.toInt()

        val seekBar = findViewById<SeekBar>(R.id.seekBarLifeStyle)
        var lifestyle = findViewById<TextView>(R.id.tvLifeStyleValue)
        val savedLifestyle = sharedPref.getString("lifestyle", "0")?.toInt()
        if (savedLifestyle != null) {
            seekBar.progress = savedLifestyle
            lifestyle.text = activityLevelDictionary[savedLifestyle]?.lifestyle ?: "Sedentary"
            lifestyleScaleFactor = activityLevelDictionary[savedLifestyle]?.scale ?: 1.2
        }

        val loseWeightOption = findViewById<RadioButton>(R.id.rbLose)
        val maintainWeightOption = findViewById<RadioButton>(R.id.rbMaintain)
        val gainWeightOption = findViewById<RadioButton>(R.id.rbGain)
        val weightValueRow = findViewById<TableRow>(R.id.trWeightGoalAmount)
        val weightValueNumber = findViewById<EditText>(R.id.etWeightGoalNumber)
        val warning = findViewById<TextView>(R.id.tvWarning)
        val radioGroup = findViewById<RadioGroup>(R.id.rgWeightGoals)

        val savedWeightGoal = sharedPref.getString("weightGoal", maintainWeightOption.id.toString())
        if (savedWeightGoal != null) {
            val selected = findViewById<RadioButton>(savedWeightGoal.toInt())
            selected.isChecked = true
            if (selected == loseWeightOption || selected == gainWeightOption)
                weightValueRow.visibility = VISIBLE
        }

        val savedWeightChange = sharedPref.getString("weightChange", "0")
        if (savedWeightChange != null) {
            weightValueNumber.setText(abs(savedWeightChange.toDouble()).toString())
            weightChange = savedWeightChange.toDouble()
        }

        if (savedSex == "M"){
            BMR = ((10 * weightInKG!!) + (6.25 * heightInCM!!) - (5 * age!!) + 5).toInt()
        } else {
            BMR = ((10 * weightInKG!!) + (6.25 * heightInCM!!) - (5 * age!!) - 161).toInt()
        }
        userBMR.text = BMR.toString()
        caloricGoal.text = calculateCaloricGoal().toString()

        loseWeightOption.setOnClickListener {
            weightValueRow.visibility = VISIBLE
        }
        gainWeightOption.setOnClickListener {
            weightValueRow.visibility = VISIBLE
        }
        maintainWeightOption.setOnClickListener {
            weightValueRow.visibility = GONE
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

        val calculateButton = findViewById<Button>(R.id.buttonCalculate)
        calculateButton.setOnClickListener{
            val selectedOption = radioGroup.checkedRadioButtonId
            val weightGoal = findViewById<RadioButton>(selectedOption)

            if (weightGoal == loseWeightOption && weightValueNumber.text.toString().isBlank())
                Toast.makeText(this, "Enter how many pounds you would like to lose.", Toast.LENGTH_SHORT).show()
            else if (weightGoal == gainWeightOption && weightValueNumber.text.toString().isBlank())
                Toast.makeText(this, "Enter how many pounds you would like to gain.", Toast.LENGTH_SHORT).show()
            else {
                if (weightGoal == loseWeightOption && weightValueNumber.text.toString().toDouble() > 2) {
                    weightChange = weightValueNumber.text.toString().toDouble() * -1
                    warning.text = "Warning: Losing more than 2 pounds a week is ill-advised."
                    warning.visibility = VISIBLE
                }
                else if (weightGoal == gainWeightOption && weightValueNumber.text.toString().toDouble() > 2) {
                    weightChange = weightValueNumber.text.toString().toDouble()
                    warning.text = "Warning: Gaining more than 2 pounds a week is ill-advised."
                    warning.visibility = VISIBLE
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

    }
    fun calculateCaloricGoal(): Int {
        var caloricChange = (weightChange * POUNDS_TO_CALORIES) / 7
        var caloricGoal = (BMR * lifestyleScaleFactor).toInt()
        caloricGoal = (caloricGoal + caloricChange).toInt()
        return caloricGoal
    }


}


