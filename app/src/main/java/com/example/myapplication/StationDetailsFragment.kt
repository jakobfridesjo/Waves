package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.caverock.androidsvg.BuildConfig
import com.example.myapplication.databinding.FragmentStationDetailBinding
import com.example.myapplication.model.Station
import com.example.myapplication.util.Constants
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class StationDetailsFragment : Fragment() {

    private var _binding: FragmentStationDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStationDetailBinding.inflate(inflater, container, false)

        val station: Station = StationDetailsFragmentArgs.fromBundle(requireArguments()).station

        mapView = binding.mapView
        if (station.geo_lat != null && station.geo_long != null) {
            setupMap(GeoPoint(station.geo_lat!!, station.geo_long!!))
            binding.mapViewStatus.visibility = View.INVISIBLE
            binding.mapViewStatusBackground.visibility = View.INVISIBLE
        } else {
            mapView.visibility = View.INVISIBLE
            binding.mapViewStatusBackground.visibility = View.VISIBLE
            binding.mapViewStatus.visibility = View.VISIBLE
        }

        binding.voteButton.setOnClickListener {
            val action = StationDetailsFragmentDirections
                .actionNavigationStationDetailsToNavigationVote(station)
            findNavController().navigate(action)
        }

        binding.backButton.setOnClickListener {
            val action = StationDetailsFragmentDirections
                .actionNavigationStationDetailsToNavigationSearch()
            findNavController().navigate(action)
        }

        binding.station = station

        return binding.root
    }

    /**
     * Setups the map view
     */
    private fun setupMap(geoPoint: GeoPoint) {
        val sharedPreferences = context?.getSharedPreferences("map_prefs", Context.MODE_PRIVATE)
        Configuration.getInstance().load(context, sharedPreferences)
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.setBackgroundColor(Color.BLACK)
        binding.mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        binding.mapView.setUseDataConnection(true)

        binding.mapView.minZoomLevel = 6.0
        binding.mapView.maxZoomLevel = 15.0

        // Setup Esri tile source (for getting the images for the map), needs a custom tile source
        // because the format is a different standard
        binding.mapView.setTileSource(object : OnlineTileSourceBase(
            "Esri",
            3,
            15,
            256,
            "",
            arrayOf(Constants.ESRI_API_BASE_URL),
            "Esri"
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                return (baseUrl
                        + MapTileIndex.getZoom(pMapTileIndex)
                        + "/" + MapTileIndex.getY(pMapTileIndex)
                        + "/" + MapTileIndex.getX(pMapTileIndex)
                        + mImageFilenameEnding)
            }
        })
        mapView.controller.setCenter(geoPoint)

        val marker = Marker(binding.mapView)
        marker.title = "${geoPoint.longitude} : ${geoPoint.latitude}"
        marker.position = geoPoint

        // Add markers to mapView
        binding.mapView.overlays.addAll(listOf(marker))
        binding.mapView.invalidate()

        // Disable map control
        mapView.isClickable = false
        mapView.isFocusable = false
    }
}
