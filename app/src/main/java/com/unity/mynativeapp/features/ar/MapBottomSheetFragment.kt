package com.unity.mynativeapp.features.ar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naver.maps.map.overlay.Overlay
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.FragmentMapBottomSheetBinding

class MapBottomSheetFragment(param: Overlay.OnClickListener) : BottomSheetDialogFragment()  {


    private var binding:FragmentMapBottomSheetBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMapBottomSheetBinding.inflate(inflater, container, false)
        val bundle = arguments
        val name = bundle?.getString("name")
        val status = bundle?.getString("status")
        val Address = bundle?.getString("Address")
        binding!!.infoAddress.setText(name)
        binding!!.infoName.setText(Address)
        binding!!.infoKind.setText(status)
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<TextView>(R.id.info_name)
        view?.findViewById<TextView>(R.id.info_address)
        view?.findViewById<TextView>(R.id.info_kind)
    }

}