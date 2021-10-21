package com.example.lyfr

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

// Declares the com.example.lyfr.DAO as a private property in the constructor. Pass in the com.example.lyfr.DAO
// instead of the whole database, because you only need access to the com.example.lyfr.DAO
class repository (application: Application){

    private var instance: repository? = null
    private var dao: DAO?
    lateinit var user: LiveData<User>


    init{
        val db = AppDatabase.getDatabase(application)
        dao = db?.dao()
        user = dao!!.getUser()
    }

    @Synchronized
    fun getInstance(application: Application): repository {
        if (instance == null) {
            instance = repository(application)
        }
        return instance as repository

    }

    @WorkerThread
    suspend fun insert(user: User){
        dao?.addUser(user)
    }

}