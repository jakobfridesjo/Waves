package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentMapBinding
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
    lateinit var geoPoints: List<GeoPoint>
    private val location = GeoPoint(65.5840799,22.1975568)
    private var _binding: FragmentMapBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        // Setup the map view
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID;
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.setBackgroundColor(Color.BLACK)
        binding.mapView.controller.animateTo(location)
        binding.mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        binding.mapView.setUseDataConnection(true)

        // Set some random locations for testing point drawing
        geoPoints = listOf(
            GeoPoint(65.584160,22.154751),
            GeoPoint(52.520008,13.404954),
            GeoPoint(39.904202,116.407394),
            GeoPoint(35.689487,139.691711),
            GeoPoint(-18.379554,46.628136),
            GeoPoint(-4.664757,-59.786157)
        )

        // Setup markers
        val markers = geoPoints.map {
            val marker = Marker(binding.mapView)
            marker.position = it
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.icon = generateCircle(20, Color.GREEN)
            marker
        }

        // Add markers to mapView
        binding.mapView.overlays.addAll(markers)
        binding.mapView.invalidate()

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
            arrayOf("https://server.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer/tile/"),
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
}