package com.example.lyfr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), LandingPageExistingUsersFragment.DataPassingInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onDataPass(data: Array<String>) {
        TODO("Not yet implemented")
    }
}