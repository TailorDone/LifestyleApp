package com.example.lyfr

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.math.pow

class UserHomeActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager : LocationManager
    private val locationPermissionCode = 2
    private lateinit var userLocation: String

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)
        getLocation()

//        var name = findViewById<TextView>(R.id.tvUserName)
//        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
//        val savedName = sharedPref.getString("name", "{username}")
//        val nameArray = savedName?.split(" ")?.toTypedArray()
//        name.text = nameArray?.get(0)?.uppercase() ?: "TO LYFR!"

        val bMIButton = findViewById<Button>(R.id.ibBMI) as ImageButton
        if(isTablet()) {
            bMIButton.setOnClickListener {
                var fragmentBMI = FragmentBMI()
                val fTrans = supportFragmentManager.beginTransaction()

                val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                val savedHeight = sharedPref.getString("height", "")
                val savedWeight = sharedPref.getString("weight", "")
                val bundle = Bundle()
                bundle.putString("height", savedHeight)
                bundle.putString("weight", savedWeight)
                fragmentBMI.arguments = bundle

                fTrans.replace(
                    R.id.mainFrame,
                    fragmentBMI,
                    "frag_bmi"
                )
                fTrans.addToBackStack(null)
                fTrans.commit()
            }
        }
        else {
            bMIButton.setOnClickListener {
                val intentBMI = Intent(this, BMIActivity::class.java).apply {
                }
                startActivity(intentBMI)
            }
        }

        val findHikeButton = findViewById<Button>(R.id.ibHikeMap)as ImageButton
        findHikeButton.setOnClickListener {
            val locUri = Uri.parse("geo:${userLocation}?q=" + Uri.encode("Hikes"))
            val intentHike = Intent(Intent.ACTION_VIEW, locUri)
            intentHike.setPackage("com.google.android.apps.maps")
            startActivity(intentHike)

        }

        val fitnessGoalsButton = findViewById<Button>(R.id.ibFitnessGoals) as ImageButton
        fitnessGoalsButton.setOnClickListener{
            val intentFitnessGoals = Intent(this, FitnessGoalsActivity::class.java).apply{
            }
            startActivity(intentFitnessGoals)
        }

        val weatherButton = findViewById<Button>(R.id.ibWeather) as ImageButton
        weatherButton.setOnClickListener{
            val intentWeather = Intent(this, WeatherActivity::class.java)
            startActivity(intentWeather)
        }

    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }
    override fun onLocationChanged(location: Location) {
        userLocation = "${location.latitude},${location.longitude}"
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }
}