package com.example.myapplication.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.SearchListItemBinding
import com.example.myapplication.model.Station

class SearchListAdapter(
    private val context: Context,
    private val searchListClickListener: SearchListClickListener,
    private val searchListLongClickListener: SearchListLongClickListener)
    : ListAdapter<Station, SearchListAdapter.ViewHolder>(SearchListDiffCallback()) {
    class ViewHolder(private var binding: SearchListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            station: Station,
            searchListClickListener:  SearchListClickListener,
            searchListLongClickListener: SearchListLongClickListener) {
            binding.station = station
            binding.clickListener = searchListClickListener
            binding.longClickListener = searchListLongClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SearchListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val background = holder.itemView.background
        if (background is RippleDrawable) {
            background.setColor(ColorStateList.valueOf(context.getColor(R.color.blue_200)))
        } else {
            holder.itemView.setBackgroundResource(R.drawable.ripple)
        }
        return holder.bind(getItem(position), searchListClickListener, searchListLongClickListener)
    }
}

class SearchListDiffCallback : DiffUtil.ItemCallback<Station>() {
    override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean {
        return oldItem.stationUUID == newItem.stationUUID
    }

    override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean {
        return oldItem == newItem
    }
}

class SearchListClickListener(val clickListener: (station: Station) -> Unit) {
    fun onClick(station: Station) = clickListener(station)
}

class SearchListLongClickListener(val clickListener: (station: Station) -> Unit) {
    fun onClick(station: Station) = clickListener(station)
}