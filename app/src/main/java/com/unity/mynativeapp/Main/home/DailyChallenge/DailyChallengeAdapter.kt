package com.unity.mynativeapp.Main.home.DailyChallenge

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.unity.mynativeapp.databinding.ItemRvDailyChallendgeBinding

class DailyChallengeAdapter(val context: Context): RecyclerView.Adapter<DailyChallengeAdapter.ViewHolder>() {

    var dataList = mutableListOf<DailyChallengeAdapter>()

    inner class ViewHolder(val binding: ItemRvDailyChallendgeBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: DailyChallengeItem){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyChallengeAdapter.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: DailyChallengeAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}