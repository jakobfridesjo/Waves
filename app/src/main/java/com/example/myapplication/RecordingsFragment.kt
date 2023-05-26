package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.adapter.RecordingsListAdapter
import com.example.myapplication.adapter.RecordingsListClickListener
import com.example.myapplication.adapter.SearchListAdapter
import com.example.myapplication.adapter.SearchListClickListener
import com.example.myapplication.adapter.SearchListLongClickListener
import com.example.myapplication.databinding.FragmentRecordingsBinding
import com.example.myapplication.model.Station
import com.example.myapplication.util.Constants
import com.example.myapplication.util.RecyclerViewDecorator
import com.example.myapplication.viewmodel.RecordingsViewModel
import com.example.myapplication.viewmodel.RecordingsViewModelFactory
import com.example.myapplication.viewmodel.SharedMiniPlayerViewModel
import java.io.File
import java.net.URL
import kotlin.coroutines.Continuation

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

        val application = requireNotNull(this.activity).application

        viewModelFactory = RecordingsViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[RecordingsViewModel::class.java]

        val recordingsListAdapter = context?.let {
            RecordingsListAdapter(
                it,
                RecordingsListClickListener { recording ->
                    val file = File(recording.path)
                    val fileUri = Uri.fromFile(file)
                    // Play recording
                    sharedMiniPlayerViewModel.startPlayer(
                        Station(
                            name = recording.name,
                            urlResolved = fileUri.toString()
                        )
                    )
                },
            )
        }

        binding.recordingsList.adapter = recordingsListAdapter
        viewModel.recordingsList.observe(viewLifecycleOwner) { recordingsList ->
            recordingsListAdapter?.submitList(recordingsList)
        }

        // Setup offset at the bottom of the list to let all list items be visible
        val bottomSpaceHeight = resources.getDimensionPixelSize(R.dimen.list_end_padding)
        val dividerHeight = resources.getDimensionPixelSize(R.dimen.list_divider_height)
        binding.recordingsList.addItemDecoration(
            RecyclerViewDecorator(bottomSpaceHeight, dividerHeight)
        )

        viewModel.getRecordings()

        return binding.root
    }

}