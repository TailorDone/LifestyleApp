package com.example.lyfr

import User
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import android.graphics.Bitmap
import android.widget.*
import android.graphics.ImageDecoder
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.core.content.FileProvider.getUriForFile
import androidx.lifecycle.lifecycleScope
import com.example.lyfr.ImageUri.latestTmpUri
import java.io.File
import java.util.*

class NewUserActivity : AppCompatActivity() {
    lateinit var stringName: EditText
    lateinit var stringCity: EditText
    lateinit var stringCountry: EditText
    lateinit var stringAge: EditText
    lateinit var stringSex: EditText
    lateinit var stringHeight: EditText
    lateinit var stringWeight: EditText

    var bitmap: Bitmap? = null

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    val source = ImageDecoder
                        .createSource(this.contentResolver, uri)
                    bitmap = ImageDecoder.decodeBitmap(source)
                    previewImage.setImageBitmap(bitmap)
                }
            }
            val intent = Intent(this, LookAtPicture::class.java)
            startActivity(intent)
        }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                previewImage.setImageURI(uri)
            }
        }

    val previewImage by lazy { findViewById<ImageView>(R.id.image_preview) }

    private fun setClickListeners() {
        findViewById<ImageButton>(R.id.imageButtonCamera).setOnClickListener { takeImage() }
        findViewById<ImageButton>(R.id.imageButtonCamera2).setOnClickListener { selectImageFromGallery() }
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return getUriForFile(
            applicationContext,
            "com.example.lyfr.provider",
            tmpFile

        )}





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        setClickListeners()


        stringName = findViewById(R.id.etName)
        stringCity = findViewById(R.id.etCity)
        stringCountry = findViewById(R.id.etCountry)
        stringAge = findViewById(R.id.editTextDate)
        stringSex = findViewById(R.id.etSex)
        stringHeight = findViewById(R.id.etHeight)
        stringWeight = findViewById(R.id.etWeight)

        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val savedName = sharedPref.getString("fname", "")
        val savedCity = sharedPref.getString("city", "CITY")
        val savedCountry = sharedPref.getString("country", "")
        val savedSex = sharedPref.getString("sex", "M/F")
        val savedAge = sharedPref.getString("age", "")
        val savedHeight = sharedPref.getString("height", "")
        val savedWeight = sharedPref.getString("weight", "")

        stringName.setText(savedName)
        stringCity.setText(savedCity)
        stringCountry.setText(savedCountry)
        stringAge.setText(savedAge)
        stringSex.setText(savedSex)
        stringHeight.setText(savedHeight)
        stringWeight.setText(savedWeight)



//        fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")
//        findViewById<ImageButton>(R.id.imageButtonCamera).setOnClickListener { selectImageFromGallery() }

        val saveProfileButton = findViewById<Button>(R.id.buttonSaveProfile)
        saveProfileButton.setOnClickListener{
            if (stringName.text.toString().isBlank() || stringCity.text.toString().isBlank() ||
                stringCountry.text.toString().isBlank() || stringAge.text.toString().isBlank() ||
                stringHeight.text.toString().isBlank()  || stringWeight.text.toString().isBlank() ||
                stringSex.text.toString().isBlank())
                Toast.makeText(this, "All fields must be completed", Toast.LENGTH_SHORT).show()

            else {
                var currentUser = User(stringName.text.toString(),
                    stringCity.text.toString(),
                    stringCountry.text.toString(),
                    stringSex.text.toString(),
                    stringAge.text.toString(),
                    stringHeight.text.toString(),
                    stringWeight.text.toString()
                )

                val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                with (sharedPref.edit()) {
                    putString("fname", currentUser.fname)
                    putString("city", currentUser.city)
                    putString("country", currentUser.country)
                    putString("sex", currentUser.sex)
                    putString("age", currentUser.ageStr)
                    putString("height", currentUser.heightStr)
                    putString("weight", currentUser.weightStr)
                    commit()
                }

                val intentSaveProfile = Intent(this, UserHomeActivity::class.java).apply {
                }
                startActivity(intentSaveProfile)
            }

        }


        }

    }