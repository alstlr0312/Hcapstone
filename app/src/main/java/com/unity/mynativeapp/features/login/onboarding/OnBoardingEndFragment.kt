package com.unity.mynativeapp.features.login.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.databinding.FragmentOnBoardingEndBinding
import com.unity.mynativeapp.features.login.onBoardingFragment
import com.unity.mynativeapp.features.signup.SignUpActivity
import com.unity.mynativeapp.network.util.ON_BOARDING


class OnBoardingEndFragment : Fragment() {
    private var _binding: FragmentOnBoardingEndBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingEndBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            onBoardingFinished()
        }
        binding.btnSignUp.setOnClickListener {
            onBoardingFinished()
            startActivity(Intent(requireActivity(), SignUpActivity::class.java))
        }

    }

    private fun onBoardingFinished() {
        MyApplication.prefUtil.setBoolean(ON_BOARDING, true)
        //supportFragmentManager.beginTransaction().remove()
        if(activity != null){
            activity?.supportFragmentManager?.beginTransaction()?.remove(onBoardingFragment)?.commit()

        }else{
            Log.d("onboarding", "activity is null")
        }

    }

    companion object {
        fun newInstance() = OnBoardingEndFragment()
    }

    override fun onResume() {
        super.onResume()
        btnSkip.visibility = View.INVISIBLE
    }

    override fun onPause() {
        super.onPause()
        btnSkip.visibility = View.VISIBLE
    }
}