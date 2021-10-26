package com.example.lyfr

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


class StepCounterActivity: AppCompatActivity(), SensorEventListener {

    var sensorManager : SensorManager? = null
    var stepSensor : Sensor? = null
    var running = false
    var todaysTotalSteps = 0f
    var stepListener : Boolean? = false
    lateinit var stepData : Steps
    lateinit var todaysDate : Date
    lateinit var tvTodaysSteps: TextView

    private val stepCounterViewModel: StepCounterViewModel by viewModels {
        StepCounterViewModel.StepModelFactory((application as LYFR_Application).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_step_counter)
        todaysDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        stepCounterViewModel.todaysSteps.observe(this, Observer { todaysSteps ->
            todaysSteps?.let{
                todaysTotalSteps = todaysSteps.steps.toFloat()
            }
        })

        stepCounterViewModel.stepData.observe(this, Observer { data ->
            data?.let{
                stepData = data
            }
        })
    }

    override fun onResume() {
        super.onResume()
        running = true
        if(stepSensor!=null)
            stepListener = sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        else{
            Toast.makeText(this, "No step sensor detected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        if(stepSensor!=null)
            saveStepData(stepData)
    }

    private fun saveStepData(stepData: Steps) {
        if(LocalDate.now().atStartOfDay().toString() == todaysDate.toString()){
            stepCounterViewModel.updateSteps(stepData)
        }else{
            stepData.steps = todaysTotalSteps.toInt()
            stepData.date = todaysDate.toString()
            stepCounterViewModel.insertSteps(stepData)
        }

    }

    override fun onSensorChanged(event: SensorEvent?) {
        todaysTotalSteps += event!!.values[0]
        tvTodaysSteps.text = ("$todaysTotalSteps")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }

}