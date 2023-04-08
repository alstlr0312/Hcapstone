package com.unity.mynativeapp.features.community


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.FragmentCommunityBinding
import com.unity.mynativeapp.features.postwrite.PostWriteActivity
import com.unity.mynativeapp.model.PostListDto


class CommunityFragment : Fragment() {
    val binding by lazy { FragmentCommunityBinding.inflate(layoutInflater) }

    private lateinit var postingRvAdapter: PostListRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){

            // 게시글 어댑터 설정
            postingRvAdapter = PostListRvAdapter(requireContext())
            rvPosting.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvPosting.adapter = postingRvAdapter
            postingRvAdapter.getListFromView(postListSample())


            // 게시물 필터 설정
            layoutFilter.setOnClickListener {
                var dialog = PostSortDialog(requireContext())
                dialog.show()
            }
        }

        // 게시물 작성
        this.requireActivity().findViewById<FloatingActionButton>(R.id.btn_write_post).setOnClickListener {
            startActivity(Intent(requireContext(), PostWriteActivity::class.java))
        }


    }



    fun postListSample(): MutableList<PostListDto>{

        var list = mutableListOf<PostListDto>()
        list.add(PostListDto("userName", "유산소", "조깅", "방금",
            "게시물 제목...", 5, 4, 3, 2))
        list.add(PostListDto("userName", "유산소", "조깅", "방금",
            "게시물 제목...", 5, 0, 0, 1))
        list.add(PostListDto("userName", "유산소", "조깅", "10분 전",
            "게시물 제목...", 3, 0, 0, 0))
        list.add(PostListDto("userName", "유산소", "조깅", "1시간 전",
            "게시물 제목...", 0, 0, 0, 0))
        list.add(PostListDto("userName", "유산소", "조깅", "2023.04.05",
            "게시물 제목...", 5, 4, 3, 2))
        list.add(PostListDto("userName", "유산소", "조깅", "2023.04.05",
            "게시물 제목...", 5, 4, 3, 2))
        list.add(PostListDto("userName", "유산소", "조깅", "2023.04.05",
            "게시물 제목...", 5, 4, 3, 2))
        return list
    }



}