package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentMiniplayerBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MiniplayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MiniplayerFragment : Fragment() {

    private lateinit var miniplayerViewModel: MiniplayerViewModel
    private var _binding: FragmentMiniplayerBinding? = null
    private lateinit var channelText: TextView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Connect ViewModel
        miniplayerViewModel = ViewModelProvider(this)[MiniplayerViewModel::class.java]

        channelText = binding.channelText
        channelText.text = miniplayerViewModel.getChannelText()

        return binding.root
    }

    /*
    * Destructor for fragment, just clear binding for now
    */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}