package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MediaPlayerService.Companion.startMediaService
import com.example.myapplication.databinding.FragmentMapBinding
import com.example.myapplication.utils.Constants.ESRI_BASE_URL
import com.example.myapplication.viewmodel.MapViewModel
import com.example.myapplication.viewmodel.MapViewModelFactory
import kotlinx.coroutines.Dispatchers
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlin.math.pow
import kotlin.math.sqrt


/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment() {

    // OpenStreeMap
    lateinit var mapController: MapController
    private var _binding: FragmentMapBinding? = null

    private lateinit var viewModel: MapViewModel
    private lateinit var viewModelFactory: MapViewModelFactory
    private lateinit var mapView: MapView
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        val appContainer = Waves.getAppContainer(requireContext())
        val stationRepository = appContainer.stationRepository
        val application = requireNotNull(this.activity).application

        viewModelFactory = MapViewModelFactory(stationRepository, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[MapViewModel::class.java]

        // For controlling the mini player
        val fragmentManager = requireActivity().supportFragmentManager
        val myFragment = fragmentManager.findFragmentByTag("fragment_miniplayer") as MiniplayerFragment?

        // Setup the map view
        setupMap()
        addMarkers()

        binding.mapView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    // Wait for map to be ready
                    if (binding.mapView.overlays.size > 0) {
                        val center = mapView.mapCenter
                        val closest: Marker? = findClosestMarker(binding.mapView.overlays.filterIsInstance<Marker>(),
                            center as GeoPoint
                        )
                        val radiusPx = binding.selector.width / 2L
                        val projection = binding.mapView.projection
                        val centerPixels = projection.toPixels(center, null)
                        val closestPixels = projection.toPixels(closest!!.position, null)
                        val distanceToCenter = sqrt(
                            (closestPixels.x - centerPixels.x).toDouble().pow(2.0)
                                    + (closestPixels.y - centerPixels.y).toDouble().pow(2.0)
                        )
                        if (distanceToCenter <= radiusPx) {
                            binding.mapView.controller.animateTo(closest.position)
                            viewModel.getStation(closest.title)
                        }
                    }
                }
            }
            false
        }

        // Handle miniplayer
        viewModel.station.observe(viewLifecycleOwner) {
            myFragment?.startPlayer(it)
            startMediaService(application, it.urlResolved)
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        mapView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    /**
     * Adds the markers to the map
     */
    private fun addMarkers() {
        // Add markers
        viewModel.stationList.observe(viewLifecycleOwner) { stationList ->
            val bitmap = generateCircle(5, Color.GREEN)
            stationList?.let {
                val markers = stationList
                    .filter { it.geo_lat != null && it.geo_long != null }
                    .map {
                        val marker = Marker(binding.mapView)
                        marker.position = GeoPoint(it.geo_lat!!, it.geo_long!!)
                        if (it.stationUUID.isNotEmpty()) {
                            marker.title = it.stationUUID
                        }
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        marker.icon = bitmap
                        marker
                    }

                // Add markers to mapView
                binding.mapView.overlays.addAll(markers)
                binding.mapView.invalidate()
            }
        }
    }

    /**
     * Setups the map view
     */
    private fun setupMap() {
        mapView = binding.mapView
        val sharedPreferences = context?.getSharedPreferences("map_prefs", Context.MODE_PRIVATE)
        Configuration.getInstance().load(context, sharedPreferences)
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.setBackgroundColor(Color.BLACK)
        binding.mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        binding.mapView.setUseDataConnection(true)
        // Disable repetition and set scrolling limits so that the map only tiles horizontally
        binding.mapView.isVerticalMapRepetitionEnabled = false
        binding.mapView.setScrollableAreaLimitLatitude(MapView.getTileSystem().maxLatitude,
            MapView.getTileSystem().minLatitude, 0);

        // Set reasonable levels of details for zoom in and out
        binding.mapView.minZoomLevel = 3.0
        binding.mapView.maxZoomLevel = 15.0

        // Setup Esri tile source (for getting the images for the map), needs a custom tile source
        // because the format is a different standard
        binding.mapView.setTileSource(object : OnlineTileSourceBase(
            "Esri",
            3,
            15,
            256,
            "",
            arrayOf(ESRI_BASE_URL),
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

        mapController = binding.mapView.controller as MapController
    }

    /*
     * Generates a drawable circle bitmap
     */
    private fun generateCircle(diameter: Int, color: Int): BitmapDrawable {

        val circleBMP = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(circleBMP)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
        paint.style = Paint.Style.FILL

        val centerX = diameter / 2f
        val centerY = diameter / 2f
        val radius = diameter / 2f

        canvas.drawCircle(centerX, centerY, radius, paint)

        return BitmapDrawable(resources, circleBMP)
    }

    /**
     * Get the closest marker
     */
    private fun findClosestMarker(markerList: List<Marker>, center: GeoPoint): Marker? {
        var closestMarker: Marker? = null
        var smallestDistance = Double.MAX_VALUE

        for (marker in markerList) {
            val distance = marker.position.distanceToAsDouble(center)
            if (distance < smallestDistance) {
                closestMarker = marker
                smallestDistance = distance
            }
        }

        return closestMarker
    }
}