package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.adapter.StationListAdapter
import com.example.myapplication.adapter.StationListClickListener
import com.example.myapplication.database.StationDatabase
import com.example.myapplication.database.StationDatabaseDao
import com.example.myapplication.databinding.FragmentFavoritesBinding
import com.example.myapplication.viewmodel.StationListViewModel
import com.example.myapplication.viewmodel.StationListViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {

    private lateinit var viewModel: StationListViewModel
    private lateinit var viewModelFactory: StationListViewModelFactory

    private lateinit var stationDatabaseDao: StationDatabaseDao

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        stationDatabaseDao = StationDatabase.getInstance(application).stationDatabaseDao

        viewModelFactory = StationListViewModelFactory(stationDatabaseDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[StationListViewModel::class.java]

        val stationListAdapter = StationListAdapter(
            StationListClickListener { station -> viewModel.onStationListItemClicked(station) }
        )

        binding.favoritesList.adapter = stationListAdapter
        viewModel.stationList.observe(viewLifecycleOwner) { stationList ->
            stationList?.let {
                stationListAdapter.submitList(stationList)
            }
        }
/*
        viewModel.navigateToStationDetail.observe(viewLifecycleOwner) { station ->
            station?.let {
                this.findNavController().navigate(
                    StationListFragmentDirections.actionStationListFragmentToStationDetailFragment(
                        station
                    )
                )
                viewModel.onStationDetailNavigated()
            }
        }
*/
        return binding.root
    }
/*
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_load_popular_stations -> {
                true
            }
            R.id.action_load_top_rated_stations -> {
                viewModel.addStation()
                true
            }
            R.id.action_load_saved_stations -> {
                viewModel.getSavedStations()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
 */
}