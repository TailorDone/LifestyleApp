package com.example.lyfr

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.core.app.ActivityCompat.requestPermissions

import androidx.annotation.NonNull
import androidx.core.location.LocationManagerCompat

import com.google.android.gms.tasks.OnCompleteListener

import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.google.android.gms.location.LocationResult

import com.google.android.gms.location.LocationCallback

import android.os.Looper

import com.google.android.gms.location.LocationRequest

import com.example.lyfr.databinding.ActivityUserHomeBinding








class UserHomeActivity : AppCompatActivity() {

    private lateinit var locationManager : LocationManager
    private val locationPermissionCode = 2
    private lateinit var userLocation: String
    var PERMISSION_ID = 44
    lateinit var mFusedLocationClient : FusedLocationProviderClient

    private lateinit var binding: ActivityUserHomeBinding

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_user_home)
        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)


        if (isTablet()) {
            loadWelcome()
            val welcomeButton = findViewById<Button>(R.id.ibWelcome) as ImageButton
            welcomeButton.setOnClickListener {
                loadWelcome()
            }
        }
        else {
            val savedName = sharedPref.getString("name", "{username}")
            val splitName = savedName?.split(" ")
            val userName = findViewById<TextView>(R.id.tvUserName)
            userName.text = splitName?.get(0)?.uppercase() ?: "USER"
        }

        val bMIButton = findViewById<Button>(R.id.ibBMI) as ImageButton
        if(isTablet()) {
            bMIButton.setOnClickListener {
                val fTransBMI = supportFragmentManager.beginTransaction()
                var fragmentBMI = FragmentBMI()

                val savedHeight = sharedPref.getString("height", "")
                val savedWeight = sharedPref.getString("weight", "")
                val bundleBMI = Bundle()
                bundleBMI.putString("height", savedHeight)
                bundleBMI.putString("weight", savedWeight)
                fragmentBMI.arguments = bundleBMI

                fTransBMI.replace(
                    R.id.mainFrame,
                    fragmentBMI,
                    "frag_bmi"
                )
                fTransBMI.addToBackStack("FragmentBMI")
                fTransBMI.commit()
            }
        }
        else {
            bMIButton.setOnClickListener {
                val intentBMI = Intent(this, BMIActivity::class.java).apply {
                }
                startActivity(intentBMI)
            }
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        val findHikeButton = findViewById<Button>(R.id.ibHikeMap)as ImageButton
        findHikeButton.setOnClickListener {
            val locUri = Uri.parse("geo:${userLocation}?q=" + Uri.encode("Hikes"))
            val intentHike = Intent(Intent.ACTION_VIEW, locUri)
            intentHike.setPackage("com.google.android.apps.maps")
            startActivity(intentHike)
        }

        val fitnessGoalsButton = findViewById<Button>(R.id.ibFitnessGoals) as ImageButton
        if(isTablet()) {
            fitnessGoalsButton.setOnClickListener {
                val fTransFitness = supportFragmentManager.beginTransaction()
                var fragmentFitness = FragmentFitnessGoals()

                val savedHeight = sharedPref.getString("height", "")
                val savedWeight = sharedPref.getString("weight", "")
                val savedAge = sharedPref.getString("age", "")
                val savedSex = sharedPref.getString("sex", "")

                val bundleBMI = Bundle()
                bundleBMI.putString("height", savedHeight)
                bundleBMI.putString("weight", savedWeight)
                bundleBMI.putString("age", savedAge)
                bundleBMI.putString("sex", savedSex)
                fragmentFitness.arguments = bundleBMI

                fTransFitness.replace(
                    R.id.mainFrame,
                    fragmentFitness,
                    "frag_fitness"
                )
                fTransFitness.addToBackStack("FragmentFitness")
                fTransFitness.commit()
            }
        }
        else {
            fitnessGoalsButton.setOnClickListener {
                val intentFitnessGoals = Intent(this, FitnessGoalsActivity::class.java).apply {
                }
                startActivity(intentFitnessGoals)
            }
        }

        val weatherButton = findViewById<Button>(R.id.ibWeather) as ImageButton
        if(isTablet()) {
            weatherButton.setOnClickListener {
                val fTransWeather = supportFragmentManager.beginTransaction()
                var fragmentWeather = FragmentWeather()

                val savedZIP = sharedPref.getString("zip", "")
                val bundleWeather = Bundle()
                bundleWeather.putString("zip", savedZIP)
                fragmentWeather.arguments = bundleWeather

                fTransWeather.replace(
                    R.id.mainFrame,
                    fragmentWeather,
                    "frag_weather"
                )
                fTransWeather.addToBackStack("FragmentWeather")
                fTransWeather.commit()
            }
        }
        else {
            weatherButton.setOnClickListener{
                val intentWeather = Intent(this, WeatherActivity::class.java)
                startActivity(intentWeather)
            }
        }
        val previewImage by lazy { findViewById<ImageButton>(R.id.image_preview) }
        if(isTablet()) {
            previewImage.setImageURI(null)
            previewImage.setImageURI(ImageUri.latestTmpUri)
        }
        else {

            previewImage.setImageURI(null)
            previewImage.setImageURI(ImageUri.latestTmpUri)

            previewImage.setOnClickListener{
                val editProfileIntent = Intent(this, NewUserActivity::class.java).apply {
                }
                startActivity(editProfileIntent)}
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last location from FusedLocationClient object
                mFusedLocationClient.lastLocation
                    .addOnCompleteListener(OnCompleteListener<Location?> { task ->
                        val location = task.result
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            userLocation = "${location.latitude},${location.longitude}"
                        }
                    })
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            // if permissions aren't available, request for permissions
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        // Initializing LocationRequest object with appropriate methods
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        // setting LocationRequest on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            userLocation = "${mLastLocation.latitude},${mLastLocation.longitude}"
        }
    }

    // method to check for permissions
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // If we want background location on Android 10.0 and higher, use:
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private fun requestPermissions() {
        requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    // method to check if location is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    // If everything is alright then
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            getLastLocation()
        }
    }

    fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }

    fun loadWelcome() {
        var fragmentWelcome = FragmentWelcome()
        val fTrans = supportFragmentManager.beginTransaction()
        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val savedName = sharedPref.getString("name", "{username}")
        val splitName = savedName?.split(" ")
        val bundle = Bundle()
        bundle.putString("username", splitName?.get(0))
        fragmentWelcome.arguments = bundle

        fTrans.replace(
            R.id.mainFrame,
            fragmentWelcome,
            "frag_welcome"
        )
        fTrans.commit()
    }
}