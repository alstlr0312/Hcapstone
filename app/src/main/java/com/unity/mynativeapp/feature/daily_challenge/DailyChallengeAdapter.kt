package com.unity.mynativeapp.feature.daily_challenge

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.databinding.ItemRvDailyChallendgeBinding
import com.unity.mynativeapp.model.DailyChallengeItem

class DailyChallengeAdapter(val context: Context): RecyclerView.Adapter<DailyChallengeAdapter.ViewHolder>() {

    var dataList = mutableListOf<DailyChallengeAdapter>()

    inner class ViewHolder(val binding: ItemRvDailyChallendgeBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: DailyChallengeItem){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}