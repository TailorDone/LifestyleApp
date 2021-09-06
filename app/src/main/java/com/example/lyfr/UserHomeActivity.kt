package com.example.lyfr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast

class UserHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)



        val BMIButton = findViewById<Button>(R.id.ibBMI) as ImageButton
                val intentBMI = Intent(this, BMIActivity::class.java).apply {
                    putExtra("AGE", )
                }
                startActivity(intentBMI)
            }
        }
    }
}