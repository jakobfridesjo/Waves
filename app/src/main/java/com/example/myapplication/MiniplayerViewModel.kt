package com.example.myapplication

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MiniplayerViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    private val channelText = "Channel text"

    /**
     * Sets the channel name for the miniplayer
     */
    fun setChannelText(text: String) {
        savedState[channelText] = text
    }

    /**
     * Gets the channel name for the miniplayer
     */
    fun getChannelText(): String {
        return savedState[channelText] ?: "No channel currently set for miniplayer"
    }

}