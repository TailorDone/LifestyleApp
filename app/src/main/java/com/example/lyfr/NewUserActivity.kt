package com.example.lyfr

import User
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import android.graphics.Bitmap
import android.widget.*
import android.graphics.ImageDecoder
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.lyfr.ImageUri.latestTmpUri
import java.io.File
import java.util.*

class NewUserActivity : AppCompatActivity() {
    lateinit var stringName: EditText
    lateinit var stringZip: EditText
    lateinit var stringAge: EditText
    lateinit var stringSex: EditText
    lateinit var stringHeight: EditText
    lateinit var stringWeight: EditText
    var currentUser = User()

    var bitmap: Bitmap? = null

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    val source = ImageDecoder
                        .createSource(this.contentResolver, uri)
                    bitmap = ImageDecoder.decodeBitmap(source)

                }
                previewImage.setImageBitmap(bitmap)
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

    private val previewImage by lazy { findViewById<ImageView>(R.id.image_preview) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        setClickListeners()

        val previewImage by lazy { findViewById<ImageView>(R.id.image_preview) }
        previewImage.setImageURI(null)
        previewImage.setImageURI(ImageUri.latestTmpUri)

        stringName = findViewById(R.id.etName)
        var labelName = findViewById<TextView>(R.id.tvName)
        stringZip = findViewById(R.id.etZip)
        var labelZip = findViewById<TextView>(R.id.tvZip)
        stringAge = findViewById(R.id.etDate)
        var labelAge = findViewById<TextView>(R.id.tvDOB)
        stringSex = findViewById(R.id.etSex)
        var labelSex = findViewById<TextView>(R.id.tvSex)
        stringHeight = findViewById(R.id.etHeight)
        var labelHeight = findViewById<TextView>(R.id.tvHeight)
        stringWeight = findViewById(R.id.etWeight)
        var labelWeight = findViewById<TextView>(R.id.tvWeight)

        var labelHashMap : HashMap<EditText, TextView> = HashMap<EditText, TextView>()
        labelHashMap.put(stringName, labelName)
        labelHashMap.put(stringZip, labelZip)
        labelHashMap.put(stringAge, labelAge)
        labelHashMap.put(stringSex, labelSex)
        labelHashMap.put(stringHeight, labelHeight)
        labelHashMap.put(stringWeight, labelWeight)

        val sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE)
        val savedName = sharedPref.getString("name", "")
        val savedZip = sharedPref.getString("zip", "")
        val savedSex = sharedPref.getString("sex", "M/F")
        val savedAge = sharedPref.getString("age", "")
        val savedHeight = sharedPref.getString("height", "")
        val savedWeight = sharedPref.getString("weight", "")

        stringName.setText(savedName)
        stringZip.setText(savedZip)
        stringAge.setText(savedAge)
        stringSex.setText(savedSex)
        stringHeight.setText(savedHeight)
        stringWeight.setText(savedWeight)

        val saveProfileButton = findViewById<Button>(R.id.buttonSaveProfile)
        saveProfileButton.setOnClickListener{
            if (stringName.text.toString().isBlank() || stringZip.text.toString().isBlank() ||
                stringAge.text.toString().isBlank() || stringHeight.text.toString().isBlank()  ||
                stringWeight.text.toString().isBlank() || stringSex.text.toString().isBlank())
                Toast.makeText(this, "All fields must be completed", Toast.LENGTH_SHORT).show()

            else {
                currentUser.name = stringName.text.toString()
                currentUser.zip = stringZip.text.toString()
                val age = stringAge.text.toString()
                currentUser.age = age.toInt()
                currentUser.sex = stringSex.text.toString()
                val height = stringHeight.text.toString()
                currentUser.height = height.toDouble()
                val weight = stringWeight.text.toString()
                currentUser.weight = weight.toDouble()

                val sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE)
                with (sharedPref.edit()) {
                    putString("name", currentUser.name)
                    putString("zip", currentUser.zip)
                    putString("sex", currentUser.sex)
                    putString("age", age)
                    putString("height", height)
                    putString("weight", weight)
                    commit()
                }

                val intentSaveProfile = Intent(this, UserHomeActivity::class.java).apply {
                }
                startActivity(intentSaveProfile)
            }
        }

        labelHashMap.forEach {
            it.key.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    it.value.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gradient_purple))
                    it.value.setTextColor(ContextCompat.getColorStateList(this, R.color.white))
                }
                else {
                    it.value.setBackgroundTintList(null)
                    it.value.setTextColor(ContextCompat.getColorStateList(this, R.color.gradient_purple))
                }
            }
        }
    }
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

        return FileProvider.getUriForFile(
            applicationContext,
            "com.example.lyfr.provider",
            tmpFile

        )}
}