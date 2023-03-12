package com.unity.mynativeapp.feature.community


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.FragmentCommunityBinding
import com.unity.mynativeapp.model.PostingRvItem


class CommunityFragment : Fragment() {
    lateinit var binding: FragmentCommunityBinding
    lateinit var postingRvAdapter: PostingRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityBinding.inflate(layoutInflater)

        postingRvAdapter = PostingRvAdapter(getPostingList(), requireContext())
        binding.recyclerViewPosting.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewPosting.adapter = postingRvAdapter

        return binding.root
    }

    fun getPostingList(): MutableList<PostingRvItem>{
        var list = mutableListOf<PostingRvItem>()
        list.add(PostingRvItem("user5", "포스팅 텍스트...", 5, 5, R.drawable.ic_profile_photo_base, R.drawable.photo01))
        list.add(PostingRvItem("user4", "포스팅 텍스트...", 4, 4, R.drawable.ic_profile_photo_base))
        list.add(PostingRvItem("user3", "포스팅 텍스트...", 3, 3, R.drawable.ic_profile_photo_base))
        list.add(PostingRvItem("user2", "포스팅 텍스트...", 2, 2, R.drawable.ic_profile_photo_base, R.drawable.photo01))
        list.add(PostingRvItem("user1", "포스팅 텍스트...", 1, 1, R.drawable.ic_profile_photo_base))
        list.add(PostingRvItem("user0", "포스팅 텍스트...", 0, 0, R.drawable.ic_profile_photo_base, R.drawable.photo01))
        return list
    }
}