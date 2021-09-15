package com.example.lyfr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lyfr.databinding.PictureLookAtBinding


class LookAtPicture : AppCompatActivity() {

    private lateinit var binding: PictureLookAtBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.picture_look_at)
        binding = PictureLookAtBinding.inflate(layoutInflater)
    }
}