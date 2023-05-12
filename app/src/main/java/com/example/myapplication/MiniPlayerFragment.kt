package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentMiniplayerBinding
import com.example.myapplication.viewmodel.SharedMiniPlayerViewModel
import com.example.myapplication.viewmodel.SharedMiniPlayerViewModelFactory

/**
 * A fragment for displaying a mini player
 */
class MiniPlayerFragment : Fragment() {

    private lateinit var viewModelFactory: SharedMiniPlayerViewModelFactory
    private lateinit var viewModel: SharedMiniPlayerViewModel
    private var _binding: FragmentMiniplayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMiniplayerBinding.inflate(inflater, container, false)

        val appContainer = Waves.getAppContainer(requireContext())
        val stationRepository = appContainer.stationRepository
        val application = requireNotNull(this.activity).application

        // Connect ViewModel
        viewModelFactory = SharedMiniPlayerViewModelFactory(stationRepository, application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[SharedMiniPlayerViewModel::class.java]

        // Add view model to layout
        binding.viewModel = viewModel

        viewModel.station.observe(viewLifecycleOwner) {
            binding.station = it[0]
        }

        viewModel.isPlaying.observe(viewLifecycleOwner) {
            binding.isPlaying = it
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) {
            binding.isFavorite = it
        }

        viewModel.isRecording.observe(viewLifecycleOwner) {
            binding.isRecording = it
        }

        viewModel.isEnabled.observe(viewLifecycleOwner) {
            binding.isEnabled = it
        }

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