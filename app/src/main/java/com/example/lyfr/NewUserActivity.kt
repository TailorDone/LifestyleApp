package com.example.lyfr

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.core.content.FileProvider.getUriForFile
import androidx.lifecycle.*
import com.example.lyfr.ImageUri.latestTmpUri
import java.util.*
import android.content.ContextWrapper
import android.graphics.*
import android.util.Log
import java.lang.Exception
import android.view.View
import androidx.activity.viewModels
import java.io.*
import androidx.lifecycle.Observer
import com.amplifyframework.core.Amplify


class NewUserActivity : AppCompatActivity() {
    lateinit var name: EditText
    lateinit var zip: EditText
    lateinit var sex : String
    lateinit var age: EditText
    lateinit var height: EditText
    lateinit var weight: EditText
    var picturePath : String = ""
    var lifestyle = 0
    var weightGoalOption = 0
    var weightChangeGoal = 0.0
    lateinit var previewImage : ImageView
    var bitmap: Bitmap? = null

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as LYFR_Application).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        setClickListeners()

        userViewModel.user.observe(this, Observer { currentUser ->
            // Update the cached copy of the user.
            currentUser?.let {
                picturePath = currentUser.profilePicturePath.toString()
                bitmap = picturePath.let { loadImageFromStorage(it) }
                name.setText(currentUser.name)
                zip.setText(currentUser.zip)
                age.setText(currentUser.age.toString())
                height.setText(currentUser.height.toString())
                weight.setText(currentUser.weight.toString())
                lifestyle = currentUser.lifestyle
                weightGoalOption = currentUser.weightGoalOption
                weightChangeGoal = currentUser.weightChangeGoal
                if (currentUser.sex == "M") {
                    var radioButtonSexM = findViewById<RadioButton>(R.id.male)
                    radioButtonSexM.isChecked = true
                } else {
                    var radioButtonSexF = findViewById<RadioButton>(R.id.female)
                    radioButtonSexF.isChecked = true
                }
            }
        })

        previewImage = findViewById(R.id.profilePicture)
        name = findViewById(R.id.etName)
        zip = findViewById(R.id.etZip)
        age = findViewById(R.id.etDate)
        height = findViewById(R.id.etHeight)
        weight = findViewById(R.id.etWeight)

        //Labels used for design
        var labelName = findViewById<TextView>(R.id.tvName)
        var labelZip = findViewById<TextView>(R.id.tvZip)
        var labelAge = findViewById<TextView>(R.id.tvDOB)
        val photoOptions = findViewById<LinearLayout>(R.id.photoOptions)
        var radioButtonSexM = findViewById<RadioButton>(R.id.male)
        var radioButtonSexF = findViewById<RadioButton>(R.id.female)
        var labelHeight = findViewById<TextView>(R.id.tvHeight)
        var labelWeight = findViewById<TextView>(R.id.tvWeight)

        var labelHashMap : HashMap<EditText, TextView> = HashMap<EditText, TextView>()
        labelHashMap.put(name, labelName)
        labelHashMap.put(zip, labelZip)
        labelHashMap.put(age, labelAge)
        labelHashMap.put(height, labelHeight)
        labelHashMap.put(weight, labelWeight)

        val saveProfileButton = findViewById<Button>(R.id.buttonSaveProfile)

        saveProfileButton.setOnClickListener{
            if (name.text.toString().isBlank() || zip.text.toString().isBlank() ||
                age.text.toString().isBlank() || height.text.toString().isBlank()  ||
                weight.text.toString().isBlank() || (!radioButtonSexF.isChecked && !radioButtonSexM.isChecked))
                Toast.makeText(this, "All fields must be completed", Toast.LENGTH_SHORT).show()

            else {
                sex = if (radioButtonSexF.isChecked){
                    "F"
                } else {
                    "M"
                }

                var user = User(
                    name = name.text.toString(),
                    zip = zip.text.toString(),
                    age = age.text.toString().toInt(),
                    sex = sex,
                    height = height.text.toString().toDouble(),
                    weight = weight.text.toString().toDouble(),
                    lifestyle = lifestyle,
                    weightGoalOption = weightGoalOption,
                    weightChangeGoal = weightChangeGoal,
                    profilePicturePath = picturePath )

                if (userViewModel.user.value == null) {
                    userViewModel.insert(user)
                } else {
                    userViewModel.update(user)
                }

                val intentSaveProfile = Intent(this, UserHomeActivity::class.java).apply {}
                startActivity(intentSaveProfile)
            }
        }

        var optionsVisible = false
        previewImage.setOnClickListener{
            if (optionsVisible) {
                photoOptions.visibility = GONE
                optionsVisible = false
            }
            else {
                photoOptions.visibility = VISIBLE
                optionsVisible = true
            }
        }


        labelHashMap.forEach {
            it.key.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    it.value.backgroundTintList = ContextCompat.getColorStateList(this, R.color.gradient_purple)
                    it.value.setTextColor(ContextCompat.getColorStateList(this, R.color.white))
                }
                else {
                    it.value.backgroundTintList = null
                    it.value.setTextColor(ContextCompat.getColorStateList(this, R.color.gradient_purple))
                }
            }
        }
    }

    private fun setClickListeners() {
        findViewById<Button>(R.id.buttonTakePhoto).setOnClickListener { takeImage() }
        findViewById<Button>(R.id.buttonChoosePhoto).setOnClickListener { selectImageFromGallery() }
    }

    val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    val source = ImageDecoder.createSource(this.contentResolver, uri)
                    bitmap = ImageDecoder.decodeBitmap(source)
                    previewImage.setImageBitmap(bitmap)
                    saveToInternalStorage(bitmap!!)
                }
            }
            val intent = Intent(this, LookAtPicture::class.java)
            startActivity(intent)
        }

    val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                previewImage.setImageURI(uri)
            }
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

        )
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap) {
        val cw = ContextWrapper(applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory = cw.getDir("imageDir", MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "profile.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (fos != null) {
                    fos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        picturePath = mypath.absolutePath
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

    //function from Jewelzqiu https://gist.github.com/jewelzqiu/c0633c9f3089677ecf85
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

    @SuppressLint("SdCardPath")
    private fun uploadFile() {
        val dbFile = applicationContext.getDatabasePath("com.example.lyfr.AppDatabase")
        var uploadedFile = File(dbFile.toString())
        Amplify.Storage.uploadFile("database", uploadedFile,
            { Log.i("LYFR_Application", "Successfully uploaded: ${it.key}") },
            { Log.e("LYFR_Application", "Upload failed", it) }
        )
    }

    override fun onPause() {
        super.onPause()
        uploadFile()
    }
}