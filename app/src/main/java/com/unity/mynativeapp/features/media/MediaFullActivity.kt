package com.unity.mynativeapp.features.media

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.MediaController
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityMediaFullBinding

class MediaFullActivity : BaseActivity<ActivityMediaFullBinding>(ActivityMediaFullBinding::inflate) {

    private var viewType = 0
    private var bitmap: Bitmap? = null
    private var uri: Uri? = null
    private lateinit var mediaController: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mediaController = MediaController(this)
        viewType = intent.getIntExtra("viewType", 0)
        uri = Uri.parse(intent.getStringExtra("uri"))

        when(viewType){
            1 -> { // 사진
                binding.imageView.setImageURI(uri)
            }
            2 -> { // 동영상
                mediaController.setAnchorView(binding.videoView)
                binding.videoView.setMediaController(mediaController)
                binding.videoView.setVideoURI(uri)
                binding.videoView.start()
            }
        }
    }
}