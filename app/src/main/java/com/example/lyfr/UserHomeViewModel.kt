package com.example.lyfr

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class UserHomeViewModel(application: Application) : AndroidViewModel(application) {
    val repository = repository().getInstance(application)
    init {
        Log.i("UserHomeViewModel", "UserHomeViewModel created!")
    }
    val userInfo: LiveData<User> = repository.user
}