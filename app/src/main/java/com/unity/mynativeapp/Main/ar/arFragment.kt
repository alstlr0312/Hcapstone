package com.unity.mynativeapp.Main.ar

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.FragmentArBinding

class ArFragment : Fragment() {
    lateinit var binding: FragmentArBinding
    var isUnityLoaded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArBinding.inflate(layoutInflater)
        binding.button3.setOnClickListener {
            isUnityLoaded = true
            val nextIntent = Intent(context, MainActivity::class.java)
            startActivity(nextIntent)
        }
        return binding.root
    }
}