package com.example.lyfr

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class NewUserViewModel(private val repository: UserInfoRepository) : ViewModel() {
    init {
        Log.i("NewUserViewModel", "NewUserViewModel created!")
    }
    val allUserInfo: LiveData<List<UserInfo>> = repository.allUserInfo.asLiveData()

    fun insert(userInfo: UserInfo) = viewModelScope.launch {
        repository.insert(userInfo)
    }
}