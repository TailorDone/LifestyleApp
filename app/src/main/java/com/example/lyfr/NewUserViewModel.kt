package com.example.lyfr

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class NewUserViewModel(application: Application) : AndroidViewModel(application) {
    var repository : Repository = Repository().getInstance(application)
    var userInfo: LiveData<User>? = repository.repoGetUser()

    init {
        viewModelScope.launch {
            Log.i("NewUserViewModel", "NewUserViewModel created!")
        }
    }


    override fun onCleared() {
        super.onCleared()
        // log the destruction of the viewmodel, use logcat to see logs
        Log.i("NewUserViewModel", "NewUserViewModel destroyed!")
    }

    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }

    fun update(user: User) = viewModelScope.launch {
        repository.update(user)
    }
//
//    fun setUser() = viewModelScope.launch {
//        userInfo = repository.repoGetUser()!!
//    }
//    fun getUser() : LiveData<User> {
//        return userInfo
//    }
}