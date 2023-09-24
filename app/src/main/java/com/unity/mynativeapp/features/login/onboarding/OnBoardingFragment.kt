package com.unity.mynativeapp.features.login.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.unity.mynativeapp.databinding.FragmentOnBoardingBinding

lateinit var vpOnBoarding: ViewPager2
lateinit var btnSkip: TextView

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    // viewpager로 fragment.xml 5개 만들기

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingBinding.inflate(layoutInflater)
        vpOnBoarding = binding.vpOnBoarding
        btnSkip = binding.tvSkip
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentList = arrayListOf(
            OnBoardingFirstFragment.newInstance(),
            OnBoardingSecondFragment.newInstance(),
            OnBoardingThirdFragment.newInstance(),
            OnBoardingFourthFragment.newInstance(),
            OnBoardingFifthFragment.newInstance(),
            OnBoardingEndFragment.newInstance()
        )

        val onBoardingAdapter = OnBoardingAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle,
        )

        binding.vpOnBoarding.adapter = onBoardingAdapter
        binding.vpOnBoarding.offscreenPageLimit = 1
        binding.vpOnBoarding.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.vpOnBoarding.setCurrentItem(0, true)
        //binding.vpOnBoarding.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayoutOnBoarding, binding.vpOnBoarding){ _, _ ->
        }.attach()



    }



}