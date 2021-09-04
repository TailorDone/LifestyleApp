package com.example.lyfr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity(), LandingPageExistingUsersFragment.DataPassingInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.buttonCreateNewUser)
        button.setOnClickListener{
            val intent = Intent(this, NewUserActivity::class.java).apply {
            }
            startActivity(intent)
        }
    }

    override fun onDataPass(data: Array<String>) {
        TODO("Not yet implemented")
    }

//     fun newUser(){
//        setContentView(R.layout.activity_new_user)
//    }
}