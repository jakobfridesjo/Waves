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
import com.example.myapplication.databinding.RecordingsListItemBinding
import com.example.myapplication.model.Recording

class RecordingsListAdapter(
    private val context: Context,
    private val recordingsClickListener: RecordingsListClickListener)
    : ListAdapter<Recording, RecordingsListAdapter.ViewHolder>(RecordingsListDiffCallback()) {
    class ViewHolder(private var binding: RecordingsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            recording: Recording,
            recordingsClickListener:  RecordingsListClickListener) {
            binding.recording = recording
            binding.clickListener = recordingsClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecordingsListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val background = holder.itemView.background
        if (background is RippleDrawable) {
            background.setColor(ColorStateList.valueOf(context.getColor(R.color.blue_200)))
        } else {
            holder.itemView.setBackgroundResource(R.drawable.ripple)
        }
        return holder.bind(getItem(position), recordingsClickListener)
    }
}

class RecordingsListDiffCallback : DiffUtil.ItemCallback<Recording>() {
    override fun areItemsTheSame(oldItem: Recording, newItem: Recording): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Recording, newItem: Recording): Boolean {
        return oldItem == newItem
    }
}

class RecordingsListClickListener(val clickListener: (recording: Recording) -> Unit) {
    fun onClick(recording: Recording) = clickListener(recording)
}