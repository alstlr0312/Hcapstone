package com.You.haveto.features.postwrite

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.You.haveto.R
import com.You.haveto.databinding.ItemSpinnerBinding

data class SpinnerModel(
    var title: String
)
class SpinnerAdapter(
    context: Context,
    @LayoutRes private val resId: Int,
    private val values: ArrayList<String>
) : ArrayAdapter<String>(context, resId, values) {

    override fun getCount() = values.size

    override fun getItem(position: Int) = values[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val title = values[position]
        try {
            binding.tvTitle.text = title
            binding.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.white))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val title = values[position]
        try {
            binding.tvTitle.text = title

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

}