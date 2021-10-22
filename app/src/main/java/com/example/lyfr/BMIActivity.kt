package com.example.lyfr

import android.content.Context
import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Observer
import kotlin.math.pow

const val POUNDS_TO_KILOGRAM = 0.454
const val INCHES_TO_METERS = 0.0254

class BMIActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmiactivity)

        val profilePic = findViewById<ImageView>(R.id.profilePicture)
        profilePic.setOnClickListener{
            val editProfileIntent = Intent(this, NewUserActivity::class.java).apply {
            }
            startActivity(editProfileIntent)
        }

        val userHeight = findViewById<TextView>(R.id.tvHeightInches)
        val userHeightMeters = findViewById<TextView>(R.id.tvHeightMeters)

        val userWeight = findViewById<TextView>(R.id.tvWeightPounds)
        val userWeightKilos = findViewById<TextView>(R.id.tvWeightKilos)

        val userBMI = findViewById<TextView>(R.id.tvBMIValue)

        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val savedHeight = sharedPref.getString("height", "")
        val savedWeight = sharedPref.getString("weight", "")
        val profilePicture = sharedPref.getString("profilePicture", "")
        val kg = savedWeight?.toDouble()?.times(POUNDS_TO_KILOGRAM)
        val meters = savedHeight?.toDouble()?.times(INCHES_TO_METERS)
        val meters_squared = meters?.pow(2)
        val BMI = (meters_squared?.let { kg?.div(it) })

        if (profilePicture != null) {
            loadImageFromStorage(profilePicture)
        }

        userHeight.setText("%.0f".format(savedHeight?.toDouble()))
        userHeightMeters.setText("%.2f".format(meters))
        userWeight.setText("%.0f".format(savedWeight?.toDouble()))
        userWeightKilos.setText("%.2f".format(kg))
        userBMI.setText("%.1f".format(BMI))
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
        paint.setAntiAlias(true)
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle((bitmap.width / 2).toFloat(),
            (bitmap.height / 2).toFloat(), (bitmap.width / 2).toFloat(), paint)
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

//    var observer = Observer<User>() {
//        fun onChanged(user: User){
//            if(user != null){
//                stringName.setText(user.name)
//                stringZip.setText(user.zip)
//                stringAge.setText(user.age)
//                stringHeight.setText(user.height.toString())
//                stringWeight.setText(user.weight.toString())
//            }
//        }
//    }
}