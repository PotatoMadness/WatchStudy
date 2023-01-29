package com.example.testevinfra

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testevinfra.databinding.ListStationItemBinding

class StationInfoAdapter : RecyclerView.Adapter<StationInfoAdapter.ViewHolder>() {
    val stationList: List<StationInfo> = mutableListOf()
    class ViewHolder(val binding: ListStationItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListStationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val station = stationList[position]
        with(holder.binding) {
            tvSnm.text = station.snm
        }
    }

    override fun getItemCount(): Int = stationList.size
}