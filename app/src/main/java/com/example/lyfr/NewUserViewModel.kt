package com.example.lyfr

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewUserViewModel(application: Application) : AndroidViewModel(application) {

    val db = AppDatabase.getDatabase(application)
    val repository = db.dao()
    var userInfo: LiveData<User>

    init {
        Log.i("NewUserViewModel", "NewUserViewModel created!")
        userInfo = repository.getUser()
    }



    fun insert(userInfo: User) = GlobalScope.launch {
        repository.addUser(userInfo)
    }
}