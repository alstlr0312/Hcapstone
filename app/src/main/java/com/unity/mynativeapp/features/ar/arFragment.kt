package com.unity.mynativeapp.features.ar

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding.Unity.setOnClickListener {
            isUnityLoaded = true
            val nextIntent = Intent(context, MainActivity::class.java)
            startActivity(nextIntent)
        }
        binding.Map.setOnClickListener {
            isUnityLoaded = true
            val nextIntent = Intent(context, MapActivity::class.java)
            startActivity(nextIntent)
        }
        return binding.root
    }
}