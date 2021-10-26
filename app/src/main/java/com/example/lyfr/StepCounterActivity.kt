package com.example.lyfr

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import java.util.*


class StepCounterActivity: AppCompatActivity(), SensorEventListener {

    var sensorManager : SensorManager? = null
    var stepSensor : Sensor? = null
    var running = false
    var todaysTotalSteps = 0
    lateinit var stepData : Steps
    lateinit var todaysDate : String
    lateinit var tvTodaysSteps : TextView

    private val stepCounterViewModel: StepCounterViewModel by viewModels {
        StepCounterViewModel.StepModelFactory((application as LYFR_Application).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                1)
        }
        tvTodaysSteps = findViewById(R.id.tvTodaysSteps)
        todaysDate = getDate()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        stepCounterViewModel.todaysSteps.observe(this, Observer { todaysSteps ->
            todaysSteps?.let{
                todaysTotalSteps = todaysSteps.steps
            }
        })

        stepData = Steps(id = 1, steps = todaysTotalSteps, date = todaysDate)

    }

    override fun onResume() {
        super.onResume()
        running = true
        if(stepSensor!=null)
           sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        else{
            Toast.makeText(this, "No step sensor detected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        running = false
        if(!running) {
            stepData.steps = todaysTotalSteps
            saveStepData(stepData)
        }
        sensorManager?.unregisterListener(this)
    }

    private fun saveStepData(stepData: Steps) {
        if(isSameDay(todaysDate)){
            stepCounterViewModel.updateSteps(stepData)
        }else{
            stepData.steps = todaysTotalSteps
            stepData.date = todaysDate
            stepCounterViewModel.insertSteps(stepData)
        }

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running) {
            todaysTotalSteps += event!!.values[0].toInt()
            tvTodaysSteps.text = "$todaysTotalSteps"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    fun isSameDay(date1: String?, date2: String? = getDate()): Boolean {
        val fmt = SimpleDateFormat("dd-MM-yyyy")
        return fmt.format(date1).equals(fmt.format(date2))
    }

    fun getDate(): String {
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = Date()
        return simpleDateFormat.format(date)
    }

}