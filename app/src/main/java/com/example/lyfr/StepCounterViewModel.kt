package com.example.lyfr

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Context.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class StepCounterViewModel(private val repository: Repository) : ViewModel() {

    var stepData : LiveData<Steps> = repository.getSteps.asLiveData()
    val todaysDate : Date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
    var todaysSteps : MutableLiveData<Steps> = repository.todaysSteps.asLiveData() as MutableLiveData<Steps>


    init {
        // log the creation of the viewmodel, use logcat to see logs
        Log.i("StepCounterViewModel", "StepCounterViewModel created!")
    }

    fun insertSteps(steps: Steps) = viewModelScope.launch {
        val stepDate = steps.date.toString()
        if(stepDate == todaysDate.toString())
            repository.insertSteps(steps)
        else{
            updateSteps(steps)
        }
    }

    fun updateSteps(steps: Steps) = viewModelScope.launch {
        repository.updateSteps(steps)
    }

    class StepModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StepCounterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StepCounterViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
