package com.example.lyfr

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.gesture.Gesture
import android.gesture.GestureLibraries
import android.gesture.GestureLibraries.fromRawResource
import android.gesture.GestureLibrary
import android.gesture.GestureOverlayView
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
import java.util.*


class StepCounterActivity: AppCompatActivity(), SensorEventListener, GestureOverlayView.OnGesturePerformedListener {

    private var sensorManager : SensorManager? = null
    private var stepSensor : Sensor? = null
    private var running = false
    private var todaysTotalSteps = 0
    private lateinit var stepData : Steps
    private lateinit var todaysDate : String
    private lateinit var tvTodaysSteps : TextView
    private lateinit var tvTotalStepCount : TextView
    private lateinit var latestDate: String
    private var rowCount : Int = 0
    private var gestureLibrary: GestureLibrary? = null
    private lateinit var gestureOverlay: GestureOverlayView

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
        tvTotalStepCount = findViewById(R.id.tvTotalStepCount)
        gestureOverlay = findViewById(R.id.gestureOverlay)

        todaysDate = getDate()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        stepCounterViewModel.todaysSteps.observe(this, { todaysSteps ->
            todaysSteps?.let{
                todaysTotalSteps = todaysSteps.steps
                stepData.steps = todaysSteps.steps
                stepData.date = todaysSteps.date
                stepData.id = todaysSteps.id
                tvTodaysSteps.text = todaysSteps.steps.toString()
            }
        })

        stepCounterViewModel.totalSteps.observe(this, { total ->
            total?.let{
                tvTotalStepCount.text = total.toString()
            }
        })

        stepCounterViewModel.stepRowCount.observe(this, { stepRowCount ->
            stepRowCount?.let{
                rowCount = stepRowCount
            }
        })

        stepData = Steps(id = 1, steps = todaysTotalSteps, date = todaysDate)

        stepCounterViewModel.latestDate.observe(this, { date ->
            date?.let{
                latestDate = date.date.toString()
                stepData.id = date.id
            }
        })

        gestureSetup()
    }

    override fun onResume() {
        super.onResume()
        running = true
        if(stepSensor!=null)
           sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_GAME)
        else{
            Toast.makeText(this, "No step sensor detected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        running = false
        if(stepSensor!=null) {
            stepData.steps = todaysTotalSteps
            saveStepData(stepData)
        }
        sensorManager?.unregisterListener(this)
    }

    private fun saveStepData(stepData: Steps) {
        if(isSameDay(todaysDate, latestDate) && rowCount > 0){
            stepData.steps = todaysTotalSteps
            stepData.date = todaysDate
            stepCounterViewModel.updateSteps(stepData)
        }else{
            stepData.steps = todaysTotalSteps
            stepData.date = todaysDate
            stepData.id += 1
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

    private fun isSameDay(date1: String?, date2: String?): Boolean {
        val d1 = SimpleDateFormat("dd-MM-yyyy").parse(date1)
        val d2 = SimpleDateFormat("dd-MM-yyyy").parse(date2)
        return (d1).equals(d2)
    }

    private fun getDate(): String {
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date = Date()
        return simpleDateFormat.format(date)
    }

    override fun onGesturePerformed(overlay: GestureOverlayView?, gesture: Gesture?) {
        val predictions = gestureLibrary?.recognize(gesture)

        predictions?.let{
            if(it.size > 0 && it[0].score > 1.0){
                val action = it[0].name
                if(action == "Circle") {
                    running = true
                    Toast.makeText(this, "Step Counter Started", Toast.LENGTH_SHORT).show()
                }else if(action == "Checkmark"){
                    running = false
                    Toast.makeText(this, "Step Counter Stopped", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun gestureSetup(){
        gestureLibrary = fromRawResource(this, R.raw.gestures)

        if(gestureLibrary?.load() == false){
            finish()
        }
        gestureOverlay.addOnGesturePerformedListener(this)
    }
}