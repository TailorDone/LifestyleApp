package com.example.lyfr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    lateinit var userData : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var mainActivityViewModel = MainActivityViewModel(application)

        setContentView(R.layout.activity_main)

        val newUserButton = findViewById<Button>(R.id.buttonCreateNewUser)

        val userObserver = Observer<User> {
            user -> userData = user
        }

        mainActivityViewModel.userInfo.observe(this, userObserver)


        if (mainActivityViewModel.userInfo.value?.name?.isNotEmpty() == true) {
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