package com.You.haveto.features.login.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.You.haveto.R
import com.You.haveto.databinding.FragmentOnBoardingThirdBinding

class OnBoardingThirdFragment : Fragment() {

    private var _binding: FragmentOnBoardingThirdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingThirdBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSkip.setOnClickListener {
            vpOnBoarding.currentItem = 5
        }
    }

    companion object {
        fun newInstance() = OnBoardingThirdFragment()
    }
}