package com.example.lyfr

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainActivityViewModel  : ViewModel() {
    init {
        // log the creation of the viewmodel, use logcat to see logs
        Log.i("MainActivityViewModel", "MainActivityViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        // log the destruction of the viewmodel, use logcat to see logs
        Log.i("MainActivityViewModel", "MainActivityViewModel destroyed!")
    }
}