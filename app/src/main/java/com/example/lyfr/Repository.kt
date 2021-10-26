package com.example.lyfr

import android.app.Application
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow


// Declares the com.example.lyfr.DAO as a private property in the constructor. Pass in the com.example.lyfr.DAO
// instead of the whole database, because you only need access to the com.example.lyfr.DAO
class Repository (private val dao: DAO){

    val getUser : Flow<User> = dao.getUser()

    @WorkerThread
    suspend fun insert(user: User){
        dao.addUser(user)
    }

    @WorkerThread
    suspend fun update(user: User) {
        dao.updateUser(user)
    }

    @WorkerThread
    suspend fun updateFitnessGoals(lifestyle: Int, weightChangeGoal: Double, weightGoalOption: Int, name: String) {
        dao.updateFitnessGoals(lifestyle, weightChangeGoal, weightGoalOption, name)
    }
}