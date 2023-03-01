package com.unity.mynativeapp.Main.community


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.unity.mynativeapp.Main.community.posting.PostingRvAdapter
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.FragmentCommunityBinding
import com.unity.mynativeapp.databinding.ItemTvSubCatogoryBinding
import java.time.LocalDate
import java.time.LocalDateTime


class CommunityFragment : Fragment() {
    val binding by lazy { FragmentCommunityBinding.inflate(layoutInflater) }
    lateinit var postingRvAdapter: PostingRvAdapter
    lateinit var subCategoryAdapter: SubCategoryAdapter
    val subCategoryList = listOf("sub1", "sub2", "sub3", "sub4", "sub5", "sub6")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        subCategoryAdapter = SubCategoryAdapter(subCategoryList, requireContext())
        binding.recyclerViewSubCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewSubCategory.adapter = subCategoryAdapter

        postingRvAdapter = PostingRvAdapter(requireContext())
        binding.recyclerViewPosting.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewPosting.adapter = postingRvAdapter
        postingRvAdapter.getListFromView(getPostingList())


        return binding.root
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

class SubCategoryAdapter(private val list: List<String>, val context: Context): RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {

    var bindingList = mutableListOf<ItemTvSubCatogoryBinding>()
    inner class ViewHolder(val binding: ItemTvSubCatogoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(title: String){

            binding.category.text = title

        }
        init{
            if(bindingList.size == 0){
                binding.category.background = context.getDrawable(R.drawable.shape_category_selected)
            }
            bindingList.add(binding)
            binding.root.setOnClickListener {
                for(i in 0 until bindingList.size){
                    bindingList[i].category.background = context.getDrawable(R.drawable.shape_category_unselected)
                }
                binding.category.background = context.getDrawable(R.drawable.shape_category_selected)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): SubCategoryAdapter.ViewHolder {
        return ViewHolder(ItemTvSubCatogoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SubCategoryAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}