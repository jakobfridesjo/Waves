package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.myapplication.databinding.FragmentStationDetailBinding

class StationDetailsDialogFragment : DialogFragment() {

    private var _binding: FragmentStationDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStationDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
}
