package com.example.lyfr

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newUserButton = findViewById<Button>(R.id.buttonCreateNewUser)
        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)

        if (sharedPref.contains("name")) {
            newUserButton.text = resources.getString(R.string.buttonContinue)
            newUserButton.setOnClickListener{
                val loginIntent = Intent(this, UserHomeActivity::class.java).apply {
                }
                startActivity(loginIntent)
            }
        } else {
            newUserButton.setOnClickListener{
                val editProfileIntent = Intent(this, NewUserActivity::class.java).apply {
                }
                startActivity(editProfileIntent)
            }
        }
    }
}