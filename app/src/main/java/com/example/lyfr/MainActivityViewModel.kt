package com.example.lyfr

import android.app.Application
import android.util.Log
import androidx.lifecycle.*

class MainActivityViewModel(application : Application) : AndroidViewModel(application) {

    var userData : LiveData<User>
    init {
        // log the creation of the viewmodel, use logcat to see logs
        Log.i("MainActivityViewModel", "MainActivityViewModel created!")
        val repository = repository().getInstance(application)
        userData = repository.user

    }

    override fun onCleared() {
        super.onCleared()
        // log the destruction of the viewmodel, use logcat to see logs
        Log.i("MainActivityViewModel", "MainActivityViewModel destroyed!")
    }
}