package com.example.lyfr

import android.app.Application

class LYFR_Application : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { Repository(database.dao()) }
}