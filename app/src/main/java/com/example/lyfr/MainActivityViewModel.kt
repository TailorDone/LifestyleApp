package com.example.lyfr

import android.app.Application
import android.util.Log
import androidx.lifecycle.*

class MainActivityViewModel(application : Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = db.dao()
    var userInfo: LiveData<User> = repository.getUser()

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