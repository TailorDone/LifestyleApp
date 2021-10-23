package com.example.lyfr

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class UserViewModel(private val repository: Repository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val user: LiveData<User> = repository.getUser.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(newUser: User) = viewModelScope.launch {
        repository.insert(newUser)
    }

    fun update(user: User) = viewModelScope.launch {
        repository.update(user)
    }

    fun updateFitnessGoals(lifestyle: Int, weightChangeGoal: Double, weightGoalOption: Int, name: String) = viewModelScope.launch {
        repository.updateFitnessGoals(lifestyle, weightChangeGoal, weightGoalOption, name)
    }
}

class UserViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}