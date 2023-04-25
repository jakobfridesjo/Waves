package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.adapter.StationListAdapter
import com.example.myapplication.adapter.StationListClickListener
import com.example.myapplication.adapter.StationListLongClickListener
import com.example.myapplication.databinding.FragmentSearchBinding
import com.example.myapplication.viewmodel.SearchViewModel
import com.example.myapplication.viewmodel.SearchViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    private lateinit var viewModel: SearchViewModel
    private lateinit var viewModelFactory: SearchViewModelFactory

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        // Connect view model
        val application = requireNotNull(this.activity).application
        viewModelFactory = SearchViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]

        val stationListAdapter = StationListAdapter(
            StationListClickListener { station -> viewModel.onStationListItemClicked(station) },
            StationListLongClickListener { station -> viewModel.onStationListItemClicked(station) }
        )

        binding.searchList.adapter = stationListAdapter
        viewModel.stationList.observe(viewLifecycleOwner) { stationList ->
            stationList?.let {
                stationListAdapter.submitList(stationList)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}