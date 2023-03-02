package com.unity.mynativeapp.Main.community.tabFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.unity.mynativeapp.Main.community.PostingRvItem
import com.unity.mynativeapp.Main.community.posting.PostingRvAdapter
import com.unity.mynativeapp.Main.community.subCategory.SubCategoryAdapter
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.FragmentTab3Binding
import java.time.LocalDateTime

class Tab3Fragment : Fragment() {

    val binding by lazy { FragmentTab3Binding.inflate(layoutInflater) }

    val categoryArray = listOf("sub1", "sub2", "sub3", "sub4", "sub5", "sub6")
    lateinit var subCategoryAdapter: SubCategoryAdapter
    lateinit var postingRvAdapter: PostingRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 서브 카테고리 어댑터 설정

        subCategoryAdapter = SubCategoryAdapter(requireContext())
        binding.rvFeedbackCategory.layoutManager = GridLayoutManager(requireContext(),4)
        binding.rvFeedbackCategory.adapter = subCategoryAdapter
        subCategoryAdapter.getListFromView(categoryArray)


        // 게시글 어댑터 설정
        postingRvAdapter = PostingRvAdapter(requireContext())
        binding.rvFeedbackPost.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvFeedbackPost.adapter = postingRvAdapter
        postingRvAdapter.getListFromView(getPostingList())
    }

    fun getPostingList(): MutableList<PostingRvItem>{

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