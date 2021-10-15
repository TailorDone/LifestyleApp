package com.example.lyfr

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

// Declares the com.example.lyfr.DAO as a private property in the constructor. Pass in the com.example.lyfr.DAO
// instead of the whole database, because you only need access to the com.example.lyfr.DAO
class repository (){
    private var instance: repository? = null
    private var dao: DAO? = null

    lateinit var user: LiveData<User>

    constructor(application: Application) : this() {
        val db: AppDatabase = AppDatabase.getDatabase(application.applicationContext)
        dao = db.dao()
        if (dao != null) {
            user = dao!!.getUsers()
        }
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