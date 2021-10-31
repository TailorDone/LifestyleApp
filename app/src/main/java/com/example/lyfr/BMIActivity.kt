package com.example.lyfr

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import androidx.lifecycle.Observer
import kotlin.math.pow

const val POUNDS_TO_KILOGRAM = 0.454
const val INCHES_TO_METERS = 0.0254


class BMIActivity : AppCompatActivity() {
    val picturePath : String? = null
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as LYFR_Application).repository)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmiactivity)

        val userHeight = findViewById<TextView>(R.id.tvHeightInches)
        val userHeightMeters = findViewById<TextView>(R.id.tvHeightMeters)

        val userWeight = findViewById<TextView>(R.id.tvWeightPounds)
        val userWeightKilos = findViewById<TextView>(R.id.tvWeightKilos)

        val userBMI = findViewById<TextView>(R.id.tvBMIValue)

        userViewModel.user.observe(this, Observer { currentUser ->
            currentUser?.let {
                val weight = currentUser.weight
                val height = currentUser.height
                val kg = weight.times(POUNDS_TO_KILOGRAM)
                val meters = height.times(INCHES_TO_METERS)
                val meters_squared = meters.pow(2)
                val BMI = (meters_squared.let { kg.div(it) })
                if (currentUser.profilePicturePath != null) {
                    loadImageFromStorage(currentUser.profilePicturePath.toString())
                }

                userHeight.text = "%.0f".format(height)
                userHeightMeters.text = ("%.2f".format(meters))
                userWeight.text =("%.0f".format(weight))
                userWeightKilos.text = ("%.2f".format(kg))
                userBMI.text = ("%.1f".format(BMI))
            }
        })

        val profilePic = findViewById<ImageView>(R.id.profilePicture)
        profilePic.setOnClickListener{
            val editProfileIntent = Intent(this, NewUserActivity::class.java).apply {
            }
            startActivity(editProfileIntent)
        }
    }

    private fun loadImageFromStorage(path: String) : Bitmap? {
        try {
            val f = File(path)
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
}