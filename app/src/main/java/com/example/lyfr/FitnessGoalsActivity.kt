package com.example.lyfr

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import kotlin.math.abs
import kotlin.math.pow

const val INCHES_TO_CENTIMETERS = 2.54
const val POUNDS_TO_CALORIES = 3500

val activityLevelDictionary = mapOf(
    0 to FitnessGoalsActivity.ActivityLevel("Sedentary", 1.2),
    1 to FitnessGoalsActivity.ActivityLevel("Lightly active", 1.375),
    2 to FitnessGoalsActivity.ActivityLevel("Moderately active", 1.55),
    3 to FitnessGoalsActivity.ActivityLevel("Very active", 1.725),
    4 to FitnessGoalsActivity.ActivityLevel("Extra active", 1.9)
)

val weightGoalDictionary = mapOf(
    0 to R.id.rbLose,
    1 to R.id.rbMaintain,
    2 to R.id.rbGain,
)

class FitnessGoalsActivity : AppCompatActivity() {
    var BMR = 0
    var lifestyleScaleFactor = 0.0
    var lifestyle = 0
    var weightChangeGoal = 0.0
    var weightGoalOption = 0
    var name : String = ""
    var sex : String = ""
    class ActivityLevel(val lifestyle: String, val scale : Double) {
    }
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as LYFR_Application).repository)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitness_goals)

        val profilePic = findViewById<ImageView>(R.id.profilePicture)
        profilePic.setOnClickListener {
            val editProfileIntent = Intent(this, NewUserActivity::class.java).apply {
            }
            startActivity(editProfileIntent)
        }

        val userBMR = findViewById<TextView>(R.id.tvBMRValue)
        val caloricGoal = findViewById<TextView>(R.id.tvCaloriesNeededValue)
        val seekBar = findViewById<SeekBar>(R.id.seekBarLifeStyle)
        var lifestyleTextView = findViewById<TextView>(R.id.tvLifeStyleValue)
        val loseWeightOption = findViewById<RadioButton>(R.id.rbLose)
        val maintainWeightOption = findViewById<RadioButton>(R.id.rbMaintain)
        val gainWeightOption = findViewById<RadioButton>(R.id.rbGain)
        val weightValueRow = findViewById<TableRow>(R.id.trWeightGoalAmount)
        val weightValueNumber = findViewById<EditText>(R.id.etWeightGoalNumber)
        val warning = findViewById<TextView>(R.id.tvWarning)
        val radioGroup = findViewById<RadioGroup>(R.id.rgWeightGoals)

        userViewModel.user.observe(this, Observer { currentUser ->
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
                val selected = weightGoalID?.let { it1 -> findViewById<RadioButton>(it1) }
                if (selected != null) {
                    selected.isChecked = true
                }
                if (selected == loseWeightOption || selected == gainWeightOption)
                    weightValueRow.visibility = VISIBLE

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

        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val profilePicture = sharedPref.getString("profilePicture", "")

        if (profilePicture != null) {
            loadImageFromStorage(profilePicture)
        }

        loseWeightOption.setOnClickListener {
            weightValueRow.visibility = VISIBLE
            weightGoalOption = 0
        }
        maintainWeightOption.setOnClickListener {
            weightValueRow.visibility = GONE
            weightGoalOption = 1
        }
        gainWeightOption.setOnClickListener {
            weightValueRow.visibility = VISIBLE
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
                    weightChangeGoal = weightValueNumber.text.toString().toDouble() * -1
                    warning.text = "Warning: Losing more than 2 pounds a week is dangerous."
                    warning.visibility = VISIBLE
                }
                else if (weightGoal == gainWeightOption && weightValueNumber.text.toString().toDouble() > 2) {
                    weightChangeGoal = weightValueNumber.text.toString().toDouble()
                    warning.text = "Warning: Gaining more than 2 pounds a week is dangerous."
                    warning.visibility = VISIBLE
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
    }
    fun calculateCaloricGoal(): Int {
        var caloricChange = (weightChangeGoal * POUNDS_TO_CALORIES) / 7
        var caloricGoal = (BMR * lifestyleScaleFactor).toInt()
        caloricGoal = (caloricGoal + caloricChange).toInt()
        return caloricGoal
    }

    private fun loadImageFromStorage(path: String) : Bitmap? {
        try {
            val f = File(path, "profile.jpg")
            var b =  BitmapFactory.decodeStream(FileInputStream(f))
            val img = findViewById<View>(R.id.profilePicture) as ImageView
            b = getCircledBitmap(b)
            img.setImageBitmap(b)
            return b
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    fun getCircledBitmap(bitmap: Bitmap): Bitmap? {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.setAntiAlias(true)
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle((bitmap.width / 2).toFloat(),
            (bitmap.height / 2).toFloat(), (bitmap.width / 2).toFloat(), paint)
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }
}