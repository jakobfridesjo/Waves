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
import com.example.myapplication.databinding.SettingsListItemBinding
import com.example.myapplication.model.Setting

class SettingsListAdapter(
    private val context: Context,
    private val settingsListClickListener: SettingsListClickListener)
    : ListAdapter<Setting, SettingsListAdapter.ViewHolder>(SettingsListDiffCallback()) {

    class ViewHolder(private var binding: SettingsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            setting: Setting,
            settingListClickListener: SettingsListClickListener
        ) {
            binding.setting = setting
            binding.clickListener = settingListClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SettingsListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)
        val background = holder.itemView.background
        if (background is RippleDrawable) {
            background.setColor(ColorStateList.valueOf(context.getColor(R.color.blue_200)))
        } else {
            holder.itemView.setBackgroundResource(R.drawable.ripple)
        }
        return holder.bind(getItem(position), settingsListClickListener)
    }
}

class SettingsListDiffCallback : DiffUtil.ItemCallback<Setting>() {
    override fun areItemsTheSame(oldItem: Setting, newItem: Setting): Boolean {
        return oldItem.settingName == newItem.settingName
    }

    override fun areContentsTheSame(oldItem: Setting, newItem: Setting): Boolean {
        return oldItem == newItem
    }
}

class SettingsListClickListener(val clickListener: (setting: Setting) -> Unit) {
    fun onClick(setting: Setting) = clickListener(setting)
}