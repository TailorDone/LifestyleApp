package com.example.lyfr

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NewUserViewModel(application: Application) : AndroidViewModel(application) {
    val repository = repository().getInstance(application)
    init {
        Log.i("NewUserViewModel", "NewUserViewModel created!")
    }
    val userInfo: LiveData<User> = repository.user


    fun insert(userInfo: User) = viewModelScope.launch {
        repository.insert(userInfo)
    }
}