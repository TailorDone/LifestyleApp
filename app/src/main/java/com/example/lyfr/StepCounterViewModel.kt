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

    var todaysSteps : MutableLiveData<Steps> = repository.todaysSteps.asLiveData() as MutableLiveData<Steps>
    var totalSteps : MutableLiveData<Int> = repository.totalSteps.asLiveData() as MutableLiveData<Int>
    var stepRowCount : LiveData<Int> = repository.stepRowCount.asLiveData()
    var latestDate : MutableLiveData<Steps> = repository.latestDate.asLiveData() as MutableLiveData<Steps>

    init {
        // log the creation of the viewmodel, use logcat to see logs
        Log.i("StepCounterViewModel", "StepCounterViewModel created!")
    }

    fun insertSteps(steps: Steps) = viewModelScope.launch {
            repository.insertSteps(steps)
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
