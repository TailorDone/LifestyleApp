package com.example.lyfr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.isDigitsOnly

class NewUserActivity : AppCompatActivity() {
    lateinit var stringName: EditText
    lateinit var stringCity: EditText
    lateinit var stringCountry: EditText
    lateinit var stringAge: EditText
    lateinit var stringSex: EditText
    lateinit var stringHeight: EditText
    lateinit var stringWeight: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        stringName = findViewById(R.id.etName)
        stringCity = findViewById(R.id.etCity)
        stringCountry = findViewById(R.id.etCountry)
        stringAge = findViewById(R.id.editTextDate)
        stringSex = findViewById(R.id.etSex)
        stringHeight = findViewById(R.id.etHeight)
        stringWeight = findViewById(R.id.etWeight)

        if (savedInstanceState != null){
            stringName.setText(savedInstanceState.getString("username"))
            stringCity.setText(savedInstanceState.getString("city"))
            stringCountry.setText(savedInstanceState.getString("country"))
            stringAge.setText(savedInstanceState.getString("age"))
            stringSex.setText(savedInstanceState.getString("sex"))
            stringHeight.setText(savedInstanceState.getString("height"))
            stringWeight.setText(savedInstanceState.getString("width"))
        }

        val saveProfileButton = findViewById<Button>(R.id.buttonSaveProfile)
        saveProfileButton.setOnClickListener{
            if (stringName.text.toString().isBlank() || stringCity.text.toString().isBlank() ||
                stringCountry.text.toString().isBlank() || stringAge.text.toString().isBlank() ||
                stringHeight.text.toString().isBlank()  || stringWeight.text.toString().isBlank() ||
                stringSex.text.toString().isBlank())
                Toast.makeText(this, "All fields must be completed", Toast.LENGTH_SHORT).show()

            else {
                val intentSaveProfile = Intent(this, NewUserActivity::class.java).apply {
                }
                startActivity(intentSaveProfile)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val name = stringName.text.toString()
        val city = stringCity.text.toString()
        val country = stringCountry.text.toString()
        val age = stringAge.text.toString()
        val sex = stringSex.text.toString()
        val height = stringHeight.text.toString()
        val width = stringWeight.text.toString()
        outState.putString("username", name)
        outState.putString("city", city)
        outState.putString("country", country)
        outState.putString("age", age)
        outState.putString("sex", sex)
        outState.putString("height", height)
        outState.putString("width", width)
    }


}