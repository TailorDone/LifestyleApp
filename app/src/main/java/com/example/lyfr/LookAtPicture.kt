package com.example.lyfr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import com.example.lyfr.databinding.PictureLookAtBinding


class LookAtPicture : AppCompatActivity() {

    private lateinit var binding: PictureLookAtBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PictureLookAtBinding.inflate(layoutInflater)
        setContentView(R.layout.picture_look_at)

        val profilePicture by lazy { findViewById<ImageView>(R.id.profilePicture) }
        profilePicture.setImageURI(null)
        profilePicture.setImageURI(ImageUri.latestTmpUri)

        profilePicture.setOnClickListener{
            val editProfileIntent = Intent(this, NewUserActivity::class.java).apply {
            }
            startActivity(editProfileIntent)}

    }
}