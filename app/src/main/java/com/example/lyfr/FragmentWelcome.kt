package com.example.lyfr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class FragmentWelcome : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var fragmentView = inflater.inflate(R.layout.fragment_welcome, container, false)
        val userName = fragmentView.findViewById<TextView>(R.id.tvUserName)
        userName.text = arguments?.getString("username") ?: "USER"
        return fragmentView
    }
}