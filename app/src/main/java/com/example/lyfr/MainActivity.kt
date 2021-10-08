package com.example.lyfr

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.activity
import com.example.lyfr.R
import com.example.lyfr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewModelFactory: MainActivityViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class.
        val binding: ActivityMainBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.main_activity,
            container,
            false
        )

        viewModelFactory = MainActivityViewModelFactory(MaingActivityArgs.fromBundle(arguments!!).score)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MainActivityViewModel::class.java)

        binding.scoreText.text = viewModel.score.toString()

        return binding.root
    }









//    override fun onCreateView(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val newUserButton = findViewById<Button>(R.id.buttonCreateNewUser)
//        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
//
//        if (sharedPref.contains("name")) {
//            newUserButton.text = resources.getString(R.string.buttonContinue)
//            newUserButton.setOnClickListener{
//                val loginIntent = Intent(this, UserHomeActivity::class.java).apply {
//                }
//                startActivity(loginIntent)
//            }
//        } else {
//            newUserButton.setOnClickListener{
//                val editProfileIntent = Intent(this, NewUserActivity::class.java).apply {
//                }
//                startActivity(editProfileIntent)
//            }
//        }
//    }
}