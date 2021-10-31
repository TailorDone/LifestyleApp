package com.example.lyfr

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.core.Amplify

class LYFR_Application : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { Repository(database.dao()) }

    override fun onCreate() {
        super.onCreate()

        try {
            Amplify.configure(applicationContext)
            Log.i("LYFR_Application", "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e("LYFR_Application", "Could not initialize Amplify", error)
        }
    }

}