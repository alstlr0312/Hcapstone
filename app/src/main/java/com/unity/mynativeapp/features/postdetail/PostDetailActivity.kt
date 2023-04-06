package com.unity.mynativeapp.features.postdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityPostDetailBinding
import com.unity.mynativeapp.features.comment.CommentRvAdapter

class PostDetailActivity : BaseActivity<ActivityPostDetailBinding>(ActivityPostDetailBinding::inflate) {

    private lateinit var mediaRvAdapter: PostDetailMediaRvAdapter // 게시물 미디어 어댑터
    private lateinit var commentRvAdapter: CommentRvAdapter // 댓글 어댑터

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mediaRvAdapter = PostDetailMediaRvAdapter(this)
        binding.rvPostMedia.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPostComment.adapter = mediaRvAdapter
        mediaRvAdapter.getListFromView(setMediaSample())

        commentRvAdapter = CommentRvAdapter()

    }

    private fun setMediaSample(): MutableList<Int>{
        var list = mutableListOf<Int>()
        list.add(R.drawable.photo01)
        list.add(R.drawable.photo01)
        list.add(R.drawable.photo01)
        list.add(R.drawable.photo01)
        return list
    }
}