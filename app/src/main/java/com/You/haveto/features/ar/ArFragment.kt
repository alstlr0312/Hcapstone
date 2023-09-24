package com.You.haveto.features.ar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.You.haveto.databinding.FragmentArBinding


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
            val intent= Intent(context, MainUnityActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivityForResult(intent, 1)
        }
        return binding.root
    }
}