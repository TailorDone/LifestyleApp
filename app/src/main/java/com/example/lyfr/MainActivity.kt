package com.example.lyfr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newUserButton = findViewById<Button>(R.id.buttonCreateNewUser)
        newUserButton.setOnClickListener{
            val editProfileIntent = Intent(this, NewUserActivity::class.java).apply {}
            startActivity(editProfileIntent)
        }
    }
}