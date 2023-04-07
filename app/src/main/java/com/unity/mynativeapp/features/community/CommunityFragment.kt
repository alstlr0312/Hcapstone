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
import com.unity.mynativeapp.model.PostingRvItem
import java.time.LocalDateTime


class CommunityFragment : Fragment() {
    val binding by lazy { FragmentCommunityBinding.inflate(layoutInflater) }

    private lateinit var postingRvAdapter: PostingRvAdapter

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
            postingRvAdapter = PostingRvAdapter(requireContext())
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



    fun postListSample(): MutableList<PostingRvItem>{

        val test = LocalDateTime.now()

        var list = mutableListOf<PostingRvItem>()
        list.add(PostingRvItem("userName", "게시물 제목...", LocalDateTime.now(), 5, 5))
        list.add(PostingRvItem("userName", "게시물 제목...", test.minusHours(1), 0, 5, 1))
        list.add(PostingRvItem("userName", "게시물 제목...", test.minusMinutes(5), 5, 5, 1))
        list.add(PostingRvItem("userName", "게시물 제목...", test.minusMonths(1), 0, 0))
        list.add(PostingRvItem("userName", "게시물 제목...", test.minusDays(5), 5, 5, 2))
        list.add(PostingRvItem("userName", "게시물 제목...", test.minusDays(5), 2, 5, 0))
        list.add(PostingRvItem("userName", "게시물 제목...", test.minusDays(5), 5, 5, 1))
        list.add(PostingRvItem("userName", "게시물 제목...", test.minusDays(5), 5, 5, 1))
        return list
    }



}