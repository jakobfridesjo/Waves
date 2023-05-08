package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.FragmentMiniplayerBinding
import com.example.myapplication.model.Station
import com.example.myapplication.viewmodel.MiniplayerViewModel
import com.example.myapplication.viewmodel.MiniplayerViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [MiniplayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MiniplayerFragment : Fragment() {

    private lateinit var viewModelFactory: MiniplayerViewModelFactory
    private lateinit var viewModel: MiniplayerViewModel
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

        //TODO TEST
        val station = Station()

        // Connect ViewModel
        viewModelFactory = MiniplayerViewModelFactory(station, stationRepository, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[MiniplayerViewModel::class.java]

        viewModel.station.observe(viewLifecycleOwner) {
            binding.station = it
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

    fun refreshUI(station: Station) {
        binding.station = station
    }
}