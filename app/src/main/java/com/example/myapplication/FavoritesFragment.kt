package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.adapter.FavoritesListAdapter
import com.example.myapplication.adapter.FavoritesListClickListener
import com.example.myapplication.adapter.FavoritesListLongClickListener
import com.example.myapplication.database.StationDatabase
import com.example.myapplication.database.StationDatabaseDao
import com.example.myapplication.databinding.FragmentFavoritesBinding
import com.example.myapplication.utils.RecyclerViewDecorator
import com.example.myapplication.viewmodel.FavoritesListViewModel
import com.example.myapplication.viewmodel.FavoritesListViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {

    private lateinit var viewModel: FavoritesListViewModel
    private lateinit var viewModelFactory: FavoritesListViewModelFactory

    private lateinit var stationDatabaseDao: StationDatabaseDao

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater)

        val appContainer = Waves.getAppContainer(requireContext())
        val stationRepository = appContainer.stationRepository
        val application = requireNotNull(this.activity).application

        viewModelFactory = FavoritesListViewModelFactory(stationRepository, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[FavoritesListViewModel::class.java]

        val bottomSpaceHeight = resources.getDimensionPixelSize(R.dimen.list_end_padding)
        val dividerHeight = resources.getDimensionPixelSize(R.dimen.list_divider_height)
        val dividerColor = ContextCompat.getColor(requireContext(), R.color.grey_light)
        binding.favoritesList.addItemDecoration(RecyclerViewDecorator(bottomSpaceHeight, dividerHeight, dividerColor))

        val favoritesListAdapter = context?.let {
            FavoritesListAdapter(
                it,
                FavoritesListClickListener { station -> viewModel.onFavoritesListItemClicked(station) },
                FavoritesListLongClickListener { station -> viewModel.onFavoritesListItemClicked(station) }
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
}