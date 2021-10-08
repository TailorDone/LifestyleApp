package com.example.lyfr

import User
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import repository

class NewUserViewModel(private val repository: repository) : ViewModel() {
    init {
        Log.i("NewUserViewModel", "NewUserViewModel created!")
    }
    val userInfo: LiveData<User> = repository.user


    fun insert(userInfo: User) = viewModelScope.launch {
        repository.insert(userInfo)
    }
}