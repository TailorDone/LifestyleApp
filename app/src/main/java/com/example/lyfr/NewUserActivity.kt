package com.example.lyfr

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
import android.widget.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.core.content.FileProvider.getUriForFile
import androidx.lifecycle.lifecycleScope
import com.example.lyfr.ImageUri.latestTmpUri
import java.util.*
import android.content.ContextWrapper
import android.graphics.*
import java.lang.Exception
import android.view.View
import java.io.*
import androidx.lifecycle.Observer


class NewUserActivity : AppCompatActivity() {
    lateinit var stringName: EditText
    lateinit var stringZip: EditText
//    lateinit var stringSex : RadioButton
    lateinit var stringAge: EditText
    lateinit var stringHeight: EditText
    lateinit var stringWeight: EditText
    lateinit var newUserViewModel: NewUserViewModel
    lateinit var previewImage : ImageView
    var picturePath = ""
    var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        setClickListeners()

        previewImage = findViewById(R.id.profilePicture)
        stringName = findViewById(R.id.etName)
        stringZip = findViewById(R.id.etZip)
//        stringSex = findViewById(R.id.etSex)
        stringAge = findViewById(R.id.etDate)
        stringHeight = findViewById(R.id.etHeight)
        stringWeight = findViewById(R.id.etWeight)

        var labelName = findViewById<TextView>(R.id.tvName)
        var labelZip = findViewById<TextView>(R.id.tvZip)
        var labelAge = findViewById<TextView>(R.id.tvDOB)
        val photoOptions = findViewById<LinearLayout>(R.id.photoOptions)
        val sexButtons = findViewById<RadioGroup>(R.id.etSex)
        var radioButtonSexM = findViewById<RadioButton>(R.id.male)
        var radioButtonSexF = findViewById<RadioButton>(R.id.female)
        var labelHeight = findViewById<TextView>(R.id.tvHeight)
        var labelWeight = findViewById<TextView>(R.id.tvWeight)

        var labelHashMap : HashMap<EditText, TextView> = HashMap<EditText, TextView>()
        labelHashMap.put(stringName, labelName)
        labelHashMap.put(stringZip, labelZip)
        labelHashMap.put(stringAge, labelAge)
        labelHashMap.put(stringHeight, labelHeight)
        labelHashMap.put(stringWeight, labelWeight)

//        val sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE)
//        var picturePath = sharedPref.getString("profilePicture", "")

        bitmap = picturePath?.let { loadImageFromStorage(it) }

        //Grab an instance of the view model
        newUserViewModel = NewUserViewModel(application)

        //Set the observer
        newUserViewModel.userInfo.observe(this, observer)

//        when (savedSex) {
//            "M" -> radioButtonSexM.isChecked = true
//            "F" -> radioButtonSexF.isChecked = true
//            else -> {
//            }
//        }


        val saveProfileButton = findViewById<Button>(R.id.buttonSaveProfile)
        saveProfileButton.setOnClickListener{
            if (stringName.text.toString().isBlank() || stringZip.text.toString().isBlank() ||
                stringAge.text.toString().isBlank() || stringHeight.text.toString().isBlank()  ||
                stringWeight.text.toString().isBlank() || (!radioButtonSexF.isChecked && !radioButtonSexM.isChecked))
                Toast.makeText(this, "All fields must be completed", Toast.LENGTH_SHORT).show()

            else {
                //saves bitmap photo
                picturePath = bitmap?.let { it1 -> saveToInternalStorage(it1) }.toString()
                var currentUser = User(
                    name = stringName.text.toString(),
                    zip = stringZip.text.toString(),
                    age = stringAge.text.toString().toInt(),
                    height = stringHeight.text.toString().toDouble(),
                    weight = stringWeight.text.toString().toDouble(),
                    sex = "M",
                    profilePicturePath = picturePath
                )
                newUserViewModel.insert(currentUser)

//                currentUser?.name ?: stringName.text.toString()
//                currentUser?.zip = stringZip.text.toString()
//                val age = stringAge.text.toString()
//                currentUser?.age = age.toInt()
//                val selectedSex = findViewById<RadioButton>(sexButtons.checkedRadioButtonId)
//                var sex : String
//                if (selectedSex == radioButtonSexM)
//                    sex = "M"
//                else sex = "F"
//                currentUser?.sex = sex
//                val height = stringHeight.text.toString()
//                currentUser?.height = height.toDouble()
//                val weight = stringWeight.text.toString()
//                currentUser?.weight = weight.toDouble()
//                currentUser?.profilePicturePath = picturePath.toString()
//
//                val sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE)
//                with (sharedPref.edit()) {
//                    putString("name", currentUser?.name)
//                    putString("zip", currentUser?.zip)
//                    putString("sex", currentUser?.sex)
//                    putString("age", age)
//                    putString("height", height)
//                    putString("weight", weight)
//                    putString("profilePicture", picturePath)
//                    commit()
//                }

                val intentSaveProfile = Intent(this, UserHomeActivity::class.java).apply {
                }
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

    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
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
        return directory.absolutePath
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

    //Create an observer that watches the LiveData<com.example.lyfr.User> object
    var observer = Observer<User>() {
        fun onChanged(user: User){
            if(user != null){
                stringName.setText(user.name)
                stringZip.setText(user.zip)
                stringAge.setText(user.age)
                stringHeight.setText(user.height.toString())
                stringWeight.setText(user.weight.toString())
            }
        }
    }
}