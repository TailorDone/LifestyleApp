package com.example.lyfr

import android.app.Application
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


// Declares the com.example.lyfr.DAO as a private property in the constructor. Pass in the com.example.lyfr.DAO
// instead of the whole database, because you only need access to the com.example.lyfr.DAO
class Repository (){
    private var instance: Repository? = null
    private var dao: DAO? = null
    private lateinit var user: LiveData<User>
    private lateinit var context: Context

    constructor(application: Application) : this() {
        val db: AppDatabase = AppDatabase.getDatabase(application)
        context = application.applicationContext
        dao = db.dao()
        if (dao != null) {
            user = dao!!.getUser()
        }
    }

    @Synchronized
    fun getInstance(application: Application): Repository {
        if (instance == null) {
            instance = Repository(application)
        }
        return instance as Repository
    }

    @WorkerThread
    suspend fun insert(user: User){
        dao?.addUser(user)
    }

    @WorkerThread
    suspend fun update(user: User) {
        dao?.updateUser(user)
    }

    fun repoGetUser(): LiveData<User>? {
        return user
    }

}