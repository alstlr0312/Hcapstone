package com.You.haveto.features.login.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.You.haveto.MyApplication
import com.You.haveto.R
import com.You.haveto.databinding.FragmentOnBoardingEndBinding
import com.You.haveto.features.login.onBoardingFragment
import com.You.haveto.features.signup.SignUpActivity
import com.You.haveto.network.util.ON_BOARDING

class OnBoardingEndFragment : Fragment() {
    private var _binding: FragmentOnBoardingEndBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            activity?.findViewById<FrameLayout>(R.id.onBoardingLayout)?.visibility = View.INVISIBLE
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