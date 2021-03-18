package com.abdulmughni.personal.openweatherandroid.core.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.abdulmughni.personal.openweatherandroid.R
import com.abdulmughni.personal.openweatherandroid.core.data.Resource
import com.abdulmughni.personal.openweatherandroid.core.utils.OnMapAndViewReadyListener
import com.abdulmughni.personal.openweatherandroid.databinding.ActivityMapsBinding
import com.abdulmughni.personal.openweatherandroid.home.HomeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(),
    GoogleMap.OnMapClickListener,
    OnMapReadyCallback,
    GoogleMap.OnCameraIdleListener,
    GoogleMap.OnMarkerClickListener,
    OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    private var lastSelectedMarker: Marker? = null
    private lateinit var map: GoogleMap
    private lateinit var locationManager: LocationManager
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var locationPermissionGranted: Boolean = false
    private lateinit var markerOption: MarkerOptions
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var marker: MarkerOptions

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        getDeviceCoordinate()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        OnMapAndViewReadyListener(mapFragment, this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        with(map) {
            uiSettings.isZoomControlsEnabled = false
            setOnMapClickListener(this@MapsActivity)
            setOnMarkerClickListener(this@MapsActivity)
        }

        val currentLoc = LatLng(lat, lng)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc))
        addMarkerToMap(currentLoc)
    }

    private fun addMarkerToMap(currentLoc: LatLng) {
        viewModel.getWeatherGPS(lat, lng).observe(this, { weather ->
            if (weather != null) {
                when (weather) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        val marker = MarkerOptions()
                            .position(currentLoc)
                            .title(weather.data?.cityName)
                            .snippet(weather.data?.detail)
                        map.addMarker(marker)
                    }
                    is Resource.Error -> {

                    }
                }
            }
        })
    }

    override fun onMapClick(point: LatLng) {
        binding.tapText.text = "tapped, point=$point"

        if (lastSelectedMarker != null) {
            val marker = MarkerOptions()
                .position(point)
                .title("Loading...")
                .snippet("Loading...")
            map.addMarker(marker)
            //map.setInfoWindowAdapter(CustomInfoWindowAdapter())
            map.moveCamera((CameraUpdateFactory.newLatLng(point)))
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.showInfoWindow()
        lastSelectedMarker = marker
        viewModel.getWeatherGPS(marker?.position?.latitude!!, marker.position?.longitude!!).observe(this, { weather ->
            if (weather != null) {
                when (weather) {
                    is Resource.Loading -> {
                        Log.d("MarkerClick", "Loading")
                    }
                    is Resource.Success -> {
                        Log.d("MarkerClick", "Success")
//                        mWeather = Weather(
//                                id = weather.data?.id,
//                                lon = weather.data?.lon,
//                                lat = weather.data?.lat,
//                                windSpeed = weather.data?.windSpeed,
//                                humidity = weather.data?.humidity,
//                                feelsLike = weather.data?.feelsLike,
//                                tempMax = weather.data?.tempMax,
//                                tempMin = weather.data?.tempMin,
//                                temperature = weather.data?.temperature,
//                                icon = weather.data?.icon,
//                                detail = weather.data?.detail,
//                                status = weather.data?.status,
//                                cityName = weather.data?.cityName,
//                                timezone = weather.data?.timezone
//                        )
                        marker.title = weather.data?.cityName
                        marker.snippet = weather.data?.status
                        marker.showInfoWindow()
                        }
                    is Resource.Error -> {

                    }
                }
            }
        })
        return true
    }


    private fun getDeviceCoordinate() {
        val criteria = Criteria()
        val provider: String? = locationManager.getBestProvider(criteria, false)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("GPSApp", "User tidak mengijinkan akses lokasi")
            return
        }
        var location: Location? = null
        if (provider != null) {
            location = locationManager.getLastKnownLocation(provider)
        }
        if (location != null) {
            lat = location.latitude
            lng = location.longitude
        }
        Toast.makeText(this, "Longitude : $lng Latitude : $lat", Toast.LENGTH_SHORT).show()
        val latLng = LatLng(lat, lng)
        binding.tapText.text = "tapped, point=$latLng"

    }

    override fun onCameraIdle() {
        if (!::map.isInitialized) return
    }

    internal inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {

        // These are both view groups containing an ImageView with id "badge" and two
        // TextViews with id "title" and "snippet".
        private val window: View = layoutInflater.inflate(R.layout.custom_info_window, null)
        private val contents: View = layoutInflater.inflate(R.layout.custom_info_contents, null)

        override fun getInfoWindow(marker: Marker): View {
            render(marker, window)
            return window
        }

        override fun getInfoContents(marker: Marker): View {
            render(marker, contents)
            return contents
        }

        private fun render(marker: Marker, view: View) {
            val badge = R.drawable.googleg_standard_color_18
            view.findViewById<ImageView>(R.id.badge).setImageResource(badge)

            // Set the title and snippet for the custom info window
            val title: String? = marker.title
            val titleUi = view.findViewById<TextView>(R.id.title)

            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                titleUi.text = SpannableString(title).apply {
                    setSpan(ForegroundColorSpan(Color.RED), 0, length, 0)
                }
            } else {
                titleUi.text = "Unknown"
            }

            val snippet: String? = marker.snippet
            val snippetUi = view.findViewById<TextView>(R.id.snippet)
            if (snippet != null && snippet.length > 0) {
//                snippetUi.text = SpannableString(snippet).apply {
//                    setSpan(ForegroundColorSpan(Color.MAGENTA), 0, 24, 0)
//                    setSpan(ForegroundColorSpan(Color.BLUE), 24, snippet.length, 0)
//                }
            } else {
                snippetUi.text = "No Description"
            }
        }
    }


}