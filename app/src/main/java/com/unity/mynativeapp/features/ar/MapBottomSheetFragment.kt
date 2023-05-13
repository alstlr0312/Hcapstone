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

class MapBottomSheetFragment(param: Overlay.OnClickListener) : BottomSheetDialogFragment()  {
    lateinit var name : String
    lateinit var address : String
    lateinit var status : String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_map_bottom_sheet, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

    }

}