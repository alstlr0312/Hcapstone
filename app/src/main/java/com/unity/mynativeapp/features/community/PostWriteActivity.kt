package com.unity.mynativeapp.features.community

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.unity.mynativeapp.databinding.ActivityPostWriteBinding

class PostWriteActivity : AppCompatActivity() {
    val binding by lazy {ActivityPostWriteBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}