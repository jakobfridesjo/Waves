package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.adapter.SearchListAdapter
import com.example.myapplication.adapter.SearchListClickListener
import com.example.myapplication.adapter.SearchListLongClickListener
import com.example.myapplication.databinding.FragmentSearchBinding
import com.example.myapplication.model.Click
import com.example.myapplication.utils.Constants
import com.example.myapplication.utils.RecyclerViewDecorator
import com.example.myapplication.viewmodel.SearchViewModel
import com.example.myapplication.viewmodel.SearchViewModelFactory
import kotlin.reflect.jvm.internal.impl.resolve.scopes.receivers.ContextClassReceiver


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    private lateinit var viewModel: SearchViewModel
    private lateinit var viewModelFactory: SearchViewModelFactory
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

        // For controlling the mini player
        val fragmentManager = requireActivity().supportFragmentManager
        val myFragment = fragmentManager.findFragmentByTag("fragment_miniplayer") as MiniplayerFragment?

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
                    myFragment?.startPlayer(station)},

                SearchListLongClickListener { station ->
                    viewModel.onSearchListItemClicked(station) }
            )
        }

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

        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.searchStations(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean { return true }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}