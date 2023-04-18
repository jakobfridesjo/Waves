package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.StationListItemBinding
import com.example.myapplication.model.Station

class StationListAdapter(val stationClickListener: StationListClickListener) : ListAdapter<Station, StationListAdapter.ViewHolder>(StationListDiffCallback()) {
    class ViewHolder(private var binding: StationListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(station: Station, stationClickListener:  StationListClickListener) {
            binding.station = station
            binding.clickListener = stationClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StationListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(getItem(position), stationClickListener)
    }
}

class StationListDiffCallback : DiffUtil.ItemCallback<Station>() {
    override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean {
        return oldItem.stationuuid == newItem.stationuuid
    }

    override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean {
        return oldItem == newItem
    }
}

class StationListClickListener(val clickListener: (movie: Station) -> Unit) {
    fun onClick(station: Station) = clickListener(station)
}