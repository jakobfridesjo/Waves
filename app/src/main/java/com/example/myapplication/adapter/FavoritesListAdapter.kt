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
import com.example.myapplication.databinding.FavoritesListItemBinding
import com.example.myapplication.model.Station

class FavoritesListAdapter(
    private val context: Context,
    private val favoritesClickListener: FavoritesListClickListener,
    private val favoritesLongClickListener: FavoritesListLongClickListener)
    : ListAdapter<Station, FavoritesListAdapter.ViewHolder>(FavoritesListDiffCallback()) {
    class ViewHolder(private var binding: FavoritesListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            station: Station,
            favoritesClickListener:  FavoritesListClickListener,
            favoritesLongClickListener: FavoritesListLongClickListener) {
            binding.station = station
            binding.clickListener = favoritesClickListener
            binding.longClickListener = favoritesLongClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoritesListItemBinding.inflate(layoutInflater, parent, false)
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
        return holder.bind(getItem(position), favoritesClickListener, favoritesLongClickListener)
    }
}

class FavoritesListDiffCallback : DiffUtil.ItemCallback<Station>() {
    override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean {
        return oldItem.stationUUID == newItem.stationUUID
    }

    override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean {
        return oldItem == newItem
    }
}

class FavoritesListClickListener(val clickListener: (station: Station) -> Unit) {
    fun onClick(station: Station) = clickListener(station)
}

class FavoritesListLongClickListener(val clickListener: (station: Station) -> Unit) {
    fun onClick(station: Station) = clickListener(station)
}