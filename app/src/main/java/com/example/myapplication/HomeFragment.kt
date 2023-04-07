package com.example.myapplication

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentHomeBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.OverlayItem

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    // OpenStreeMap
    lateinit var mapController: MapController
    lateinit var overlayItem: ArrayList<OverlayItem>
    val location = GeoPoint(65.5840799,22.1975568)
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID;
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.controller.animateTo(location)
        binding.mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        binding.mapView.setUseDataConnection(true)
        binding.mapView.setTileSource(TileSourceFactory.WIKIMEDIA)
        mapController = binding.mapView.controller as MapController
        mapController.setCenter(location)
        mapController.zoomTo(15)

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}