package com.example.lyfr

import AppDatabase
import User
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import repository
import java.lang.IllegalArgumentException

class MainActivityViewModel(repository: repository) : ViewModel() {
    var userData : LiveData<User>
    init {
        // log the creation of the viewmodel, use logcat to see logs
        Log.i("MainActivityViewModel", "MainActivityViewModel created!")
        userData = repository.user
    }

    override fun onCleared() {
        super.onCleared()
        // log the destruction of the viewmodel, use logcat to see logs
        Log.i("MainActivityViewModel", "MainActivityViewModel destroyed!")
    }
}