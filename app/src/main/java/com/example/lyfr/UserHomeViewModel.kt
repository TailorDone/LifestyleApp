package com.example.lyfr

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData

class UserHomeViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repository = db.dao()
    val userInfo : MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    init {
        Log.i("UserHomeViewModel", "UserHomeViewModel created!")
    }
}