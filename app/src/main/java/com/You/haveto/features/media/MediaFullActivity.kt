package com.You.haveto.features.media

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import com.bumptech.glide.Glide
import com.You.haveto.R
import com.You.haveto.config.BaseActivity
import com.You.haveto.databinding.ActivityMediaFullBinding

class MediaFullActivity : BaseActivity<ActivityMediaFullBinding>(ActivityMediaFullBinding::inflate) {

    private var viewType = 0
    private var url: String? = null
    private var uri: Uri? = null
    private lateinit var mediaController: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mediaController = MediaController(this)
        viewType = intent.getIntExtra("viewType", 0)
        when(viewType){
            1 -> { // uri 사진
                uri = Uri.parse(intent.getStringExtra("uri"))
                binding.imageView.setImageURI(uri)
            }
            2 -> { // uri 동영상
                uri = Uri.parse(intent.getStringExtra("uri"))
                mediaController.setAnchorView(binding.videoView)
                binding.videoView.setMediaController(mediaController)
                binding.videoView.setVideoURI(uri)
                binding.videoView.start()
            }
            3 -> { // url 사진
                url = intent.getStringExtra("url")
                Glide.with(binding.imageView)
                    .load(url)
                    .placeholder(R.color.main_black_dark)
                    .error(R.drawable.ic_image_failed)
                    .fallback(R.color.main_black_dark)
                    .into(binding.imageView)
            }
            4 -> { // url 동영상
                url = intent.getStringExtra("url")

            }
        }

    }
}