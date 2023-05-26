package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.adapter.SearchListAdapter
import com.example.myapplication.adapter.SearchListClickListener
import com.example.myapplication.adapter.SearchListLongClickListener
import com.example.myapplication.databinding.FragmentSearchBinding
import com.example.myapplication.util.RecyclerViewDecorator
import com.example.myapplication.viewmodel.SearchViewModel
import com.example.myapplication.viewmodel.SearchViewModelFactory
import com.example.myapplication.viewmodel.SharedMiniPlayerViewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    private lateinit var viewModel: SearchViewModel
    private lateinit var viewModelFactory: SearchViewModelFactory
    private val sharedMiniPlayerViewModel: SharedMiniPlayerViewModel by activityViewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        val appContainer = Waves.getAppContainer(requireContext())
        val stationRepository = appContainer.stationRepository
        val application = requireNotNull(this.activity).application

        viewModelFactory = SearchViewModelFactory(stationRepository, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]

        val searchListAdapter = context?.let {
            SearchListAdapter(
                it,
                SearchListClickListener { station ->
                    viewModel.onSearchListItemClicked(station)
                    // Post Click
                    viewModel.postClick(station)

                    // Play channel
                    sharedMiniPlayerViewModel.startPlayer(station)
                    sharedMiniPlayerViewModel.setVisible()
                    },

                SearchListLongClickListener { station ->
                    sharedMiniPlayerViewModel.setInvisible()
                    val action = SearchFragmentDirections
                        .actionNavigationSearchToNavigationStationDetails(station)

                    findNavController().navigate(action)
                    true
                }
            )
        }

        if (sharedMiniPlayerViewModel.isPlaying.value == true) {
            sharedMiniPlayerViewModel.setVisible()
        }

        // Setup offset at the end of the list
        val bottomSpaceHeight = resources.getDimensionPixelSize(R.dimen.list_end_padding)
        val dividerHeight = resources.getDimensionPixelSize(R.dimen.list_divider_height)
        binding.searchList.addItemDecoration(RecyclerViewDecorator(bottomSpaceHeight, dividerHeight))

        binding.searchList.adapter = searchListAdapter
        viewModel.searchList.observe(viewLifecycleOwner) { searchList ->
            searchList?.let {
                searchListAdapter?.submitList(searchList)
                binding.searchList.invalidateItemDecorations()
            }
        }

        // Lets the user search for stations
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.searchStations(query)
                }
                return true
            }
            // Only search when text is submitted, not as soon as it's typed
            override fun onQueryTextChange(newText: String?): Boolean { return true }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}