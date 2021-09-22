package com.example.lyfr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.lyfr.databinding.PictureLookAtBinding


class LookAtPicture : AppCompatActivity() {

    private lateinit var binding: PictureLookAtBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PictureLookAtBinding.inflate(layoutInflater)
        setContentView(R.layout.picture_look_at)

        val previewImage by lazy { findViewById<ImageButton>(R.id.image_preview) }
        previewImage.setImageURI(null)
        previewImage.setImageURI(ImageUri.latestTmpUri)

        val profileButton = findViewById<ImageButton>(R.id.image_preview)
        profileButton.setOnClickListener{
            val editProfileIntent = Intent(this, NewUserActivity::class.java).apply {
            }
            startActivity(editProfileIntent)}

    }
}