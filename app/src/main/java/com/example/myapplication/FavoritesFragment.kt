package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.adapter.FavoritesListAdapter
import com.example.myapplication.adapter.FavoritesListClickListener
import com.example.myapplication.adapter.FavoritesListLongClickListener
import com.example.myapplication.databinding.FragmentFavoritesBinding
import com.example.myapplication.util.RecyclerViewDecorator
import com.example.myapplication.viewmodel.FavoritesViewModel
import com.example.myapplication.viewmodel.FavoritesViewModelFactory
import com.example.myapplication.viewmodel.SharedMiniPlayerViewModel

/**
 * A fragment for displaying favorite stations in a recycler view list
 */
class FavoritesFragment : Fragment() {

    private lateinit var viewModel: FavoritesViewModel
    private lateinit var viewModelFactory: FavoritesViewModelFactory
    private val sharedMiniPlayerViewModel: SharedMiniPlayerViewModel by activityViewModels()

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater)

        // Get global state
        val appContainer = Waves.getAppContainer(requireContext())
        val stationRepository = appContainer.stationRepository
        val application = requireNotNull(this.activity).application

        // Bind view model
        viewModelFactory = FavoritesViewModelFactory(stationRepository, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[FavoritesViewModel::class.java]

        // Setup offset at the end of the list
        val bottomSpaceHeight = resources.getDimensionPixelSize(R.dimen.list_end_padding)
        val dividerHeight = resources.getDimensionPixelSize(R.dimen.list_divider_height)
        binding.favoritesList.addItemDecoration(RecyclerViewDecorator(bottomSpaceHeight, dividerHeight))

        val favoritesListAdapter = context?.let {
            FavoritesListAdapter(
                it,
                FavoritesListClickListener { station -> sharedMiniPlayerViewModel.startPlayer(station) },
                FavoritesListLongClickListener { station ->
                    // Hide the mini player and show station details
                    sharedMiniPlayerViewModel.setInvisible()
                    val action = SearchFragmentDirections
                        .actionNavigationSearchToNavigationStationDetails(station)

                    findNavController().navigate(action)
                    true
                }
            )
        }

        binding.favoritesList.adapter = favoritesListAdapter
        viewModel.favoritesList.observe(viewLifecycleOwner) { favoritesList ->
            favoritesList?.let {
                favoritesListAdapter?.submitList(favoritesList)
                binding.favoritesList.invalidateItemDecorations()
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSavedFavorites()
    }
}