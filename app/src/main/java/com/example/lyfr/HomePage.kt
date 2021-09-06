package com.example.lyfr

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val username = sharedPref.getString("fname", "hi")
        var firstName = findViewById<TextView>(R.id.homepageNAME)
        firstName.text = username
    }
}