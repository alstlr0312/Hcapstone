package com.unity.mynativeapp.Main.community.tabFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.unity.mynativeapp.Main.community.PostingRvItem
import com.unity.mynativeapp.Main.community.posting.PostingRvAdapter
import com.unity.mynativeapp.Main.community.subCategory.SubCategoryAdapter
import com.unity.mynativeapp.databinding.FragmentTabBinding
import java.time.LocalDateTime
import kotlin.properties.Delegates


class TabFragment() : Fragment() {

    val binding by lazy {FragmentTabBinding.inflate(layoutInflater)}

    lateinit var postingRvAdapter: PostingRvAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //arguments?.let {
        //            categoryNum = it.getInt("num", 0)
        //        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 서브 카테고리 어댑터 설정
        //subCategoryAdapter = SubCategoryAdapter(requireContext())
        //binding.recyclerViewSubCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        //binding.recyclerViewSubCategory.adapter = subCategoryAdapter


        // 게시글 어댑터 설정
        postingRvAdapter = PostingRvAdapter(requireContext())
        binding.rvTotalPost.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTotalPost.adapter = postingRvAdapter
        postingRvAdapter.getListFromView(getPostingList())


    }

    companion object{
        //fun newInstance(num: Int) =
        //            TabFragment().apply {
        //                arguments = Bundle().apply {
        //                    putInt("num", num)
        //                }
        //            }
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