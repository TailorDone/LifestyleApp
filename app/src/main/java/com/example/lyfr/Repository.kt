package com.example.lyfr

import android.app.Application
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


// Declares the com.example.lyfr.DAO as a private property in the constructor. Pass in the com.example.lyfr.DAO
// instead of the whole database, because you only need access to the com.example.lyfr.DAO
class Repository (private val dao: DAO){

    val getUser : Flow<User> = dao.getUser()
    val todaysDate : Date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())
    val todaysSteps: Flow<Steps> = dao.getTodaysSteps(todaysDate.toString())
    val totalSteps: Flow<Int> = dao.getTotalSteps()
    val stepRowCount : Flow<Int> = dao.getStepRowCount()

//    @Synchronized
//    fun getInstance(application: Application): Repository {
//        if (instance == null) {
//            instance = Repository(application)
//        }
//        return instance as Repository
//    }

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

    @WorkerThread
    suspend fun insertSteps(steps: Steps){
        dao.insertSteps(steps)
    }

    @WorkerThread
    suspend fun updateSteps(steps: Steps){
        dao.updateSteps(steps)
    }

}