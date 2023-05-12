package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentRecordingsBinding
import com.example.myapplication.utils.RecyclerViewDecorator
import com.example.myapplication.viewmodel.RecordingsViewModel
import com.example.myapplication.viewmodel.RecordingsViewModelFactory
import com.example.myapplication.viewmodel.SharedMiniPlayerViewModel

/**
 * A fragment for displaying favorite stations
 */
class RecordingsFragment : Fragment() {

    private val sharedMiniPlayerViewModel: SharedMiniPlayerViewModel by activityViewModels()
    private lateinit var viewModelFactory: RecordingsViewModelFactory
    private lateinit var viewModel: RecordingsViewModel


    private var _binding: FragmentRecordingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecordingsBinding.inflate(inflater)

        val appContainer = Waves.getAppContainer(requireContext())
        val stationRepository = appContainer.stationRepository
        val application = requireNotNull(this.activity).application

        viewModelFactory = RecordingsViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[RecordingsViewModel::class.java]

        val bottomSpaceHeight = resources.getDimensionPixelSize(R.dimen.list_end_padding)
        val dividerHeight = resources.getDimensionPixelSize(R.dimen.list_divider_height)
        binding.recordingsList.addItemDecoration(RecyclerViewDecorator(bottomSpaceHeight, dividerHeight))
        return binding.root
    }

}