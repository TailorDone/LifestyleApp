package com.example.lyfr

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class UserHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)

        var userFirstName = findViewById<TextView>(R.id.tvUserName)
        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val savedName = sharedPref.getString("fname", "{username}")
        userFirstName.setText(savedName)

        val BMIButton = findViewById<Button>(R.id.ibBMI) as ImageButton
        BMIButton.setOnClickListener {
            val intentBMI = Intent(this, BMIActivity::class.java).apply {
            }
            startActivity(intentBMI)
        }
    }
}

