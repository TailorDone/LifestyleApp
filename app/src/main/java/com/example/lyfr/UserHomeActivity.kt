package com.example.lyfr

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.core.app.ActivityCompat.requestPermissions
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class UserHomeActivity : AppCompatActivity() {
    lateinit var mFusedLocationClient : FusedLocationProviderClient
    var userLocation = ""

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home)
        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val profilePicture = sharedPref.getString("profilePicture", "")

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

        val bMIButton = findViewById<ImageButton>(R.id.ibBMI)
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


        val findHikeButton = findViewById<Button>(R.id.ibHikeMap)as ImageButton
        findHikeButton.setOnClickListener {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            val locationEnabled = getLocation()
            if (locationEnabled) {
                val locUri = Uri.parse("geo:${userLocation}?q=" + Uri.encode("Hikes"))
                val intentHike = Intent(Intent.ACTION_VIEW, locUri)
                intentHike.setPackage("com.google.android.apps.maps")
                startActivity(intentHike)
            }
            else
                Toast.makeText(this, "Location must be enabled.", Toast.LENGTH_SHORT).show()

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

        val profilePictureButton = findViewById<ImageView>(R.id.profilePicture)
        if (profilePicture != null) {
            loadImageFromStorage(profilePicture)
        }
        profilePictureButton.setOnClickListener{
            val editProfileIntent = Intent(this, NewUserActivity::class.java).apply {
            }
            startActivity(editProfileIntent)
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLocation() : Boolean {
        // check if permissions are given
        if (checkPermissions()) {
                // getting last location from FusedLocationClient object
                val task = mFusedLocationClient.lastLocation
                task.addOnSuccessListener{
                    val location = task.result
                    userLocation = "${location.latitude},${location.longitude}"
                }
            return true
        } else {
            // if permissions aren't available, request for permissions
            requestPermissions()
            return false
        }
    }

    // method to check for permissions
    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        // If we want background location on Android 10.0 and higher, use:
    }

    // method to request for permissions
    private fun requestPermissions() {
        requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 101
        )
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

    private fun loadImageFromStorage(path: String) : Bitmap? {
        try {
            val f = File(path, "profile.jpg")
            var b =  BitmapFactory.decodeStream(FileInputStream(f))
            val img = findViewById<View>(R.id.profilePicture) as ImageView
            b = getCircledBitmap(b)
            img.setImageBitmap(b)
            return b
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    fun getCircledBitmap(bitmap: Bitmap): Bitmap? {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle((bitmap.width / 2).toFloat(),
            (bitmap.height / 2).toFloat(), (bitmap.width / 2).toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }
}