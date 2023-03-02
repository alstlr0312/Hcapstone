package com.unity.mynativeapp.Main.community


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.unity.mynativeapp.Main.BaseActivity
import com.unity.mynativeapp.Main.community.posting.PostingRvAdapter
import com.unity.mynativeapp.Main.community.subCategory.SubCategoryAdapter
import com.unity.mynativeapp.Main.community.tabFragments.Tab2Fragment
import com.unity.mynativeapp.Main.community.tabFragments.Tab3Fragment
import com.unity.mynativeapp.Main.community.tabFragments.TabFragment
import com.unity.mynativeapp.databinding.FragmentCommunityBinding
import java.time.LocalDateTime


class CommunityFragment : Fragment() {
    val binding by lazy { FragmentCommunityBinding.inflate(layoutInflater) }


    val mainCategoryList = listOf("전체", "main2", "main3")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){

            // 뷰페이저
            viewPager2.adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)

            // 탭 타이틀 셋팅, 뷰페이저 연결
            TabLayoutMediator(layoutTabMain, viewPager2) { tab, position ->
                tab.text = mainCategoryList[position]
            }.attach()

        }

    }

    inner class ViewPagerAdapter(supportFragmentManager: FragmentManager, lifecycle: Lifecycle)
        : FragmentStateAdapter(supportFragmentManager, lifecycle) {

        val PAGENUMBER = 3
        override fun getItemCount(): Int = PAGENUMBER //3

        override fun createFragment(position: Int): Fragment {

            return when(position){
                0 -> {
                    TabFragment() }
                1 -> {
                    Tab2Fragment()}
                2 -> {
                    Tab3Fragment()}
                else -> {
                    TabFragment()}
            }
        }

    }
}