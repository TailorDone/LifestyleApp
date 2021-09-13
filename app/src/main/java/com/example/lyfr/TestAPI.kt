package com.example.lyfr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class TestAPI : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_api)

        val urlString: TextView = findViewById(R.id.testAPI)
        var userInfo = intent.getStringExtra("weatherJSON")
        urlString.text = userInfo
    }
}