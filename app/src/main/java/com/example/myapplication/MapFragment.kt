package com.example.myapplication

import android.annotation.SuppressLint
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
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment() {

    // OpenStreeMap
    lateinit var mapController: MapController
    private var _binding: FragmentMapBinding? = null
    private val location = GeoPoint(65.5840799,22.1975568)

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

        // Connect view model
        val application = requireNotNull(this.activity).application
        viewModelFactory = MapViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)[MapViewModel::class.java]

        mapView = binding.mapView

        // Setup the map view
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.setBackgroundColor(Color.BLACK)
        binding.mapView.controller.animateTo(location)
        binding.mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        binding.mapView.setUseDataConnection(true)

        binding.mapView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    val center = GeoPoint(
                        mapView.boundingBox.centerLatitude,
                        mapView.boundingBox.centerLongitude
                    )
                    val closest: Marker? = findClosestMarker(binding.mapView.overlays.filterIsInstance<Marker>(), center)
                    binding.mapView.controller.animateTo(closest!!.position)
                    startMediaService(application, closest.title)
                }
            }
            false
        }

        // Add markers
        viewModel.stationList.observe(viewLifecycleOwner) { stationList ->
            stationList?.let {
                val bitmap = generateCircle(10, Color.GREEN)
                val markers = stationList
                    // filter out elements with null geo_lat or geo_long
                    .filter { it.geo_lat != null && it.geo_long != null }
                    .map {
                        val marker = Marker(binding.mapView)
                        marker.position = GeoPoint(it.geo_lat!!, it.geo_long!!)
                        marker.title = it.url
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                        marker.icon = bitmap
                        marker
                    }

                // Add markers to mapView
                binding.mapView.overlays.addAll(markers)
                binding.mapView.invalidate()
            }
        }

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
        mapController.setCenter(location)

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