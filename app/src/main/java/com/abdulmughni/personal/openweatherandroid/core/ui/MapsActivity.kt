package com.abdulmughni.personal.openweatherandroid.core.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.abdulmughni.personal.openweatherandroid.R
import com.abdulmughni.personal.openweatherandroid.core.data.Resource
import com.abdulmughni.personal.openweatherandroid.core.utils.OnMapAndViewReadyListener
import com.abdulmughni.personal.openweatherandroid.databinding.ActivityMapsBinding
import com.abdulmughni.personal.openweatherandroid.home.HomeFragment.Companion.CODE_CELSIUS_REQUEST
import com.abdulmughni.personal.openweatherandroid.home.HomeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

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
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        getDeviceCoordinate()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
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
        viewModel.getWeather(lat, lng, CODE_CELSIUS_REQUEST).observe(this, { weather ->
            if (weather != null) {
                when (weather) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        val marker = MarkerOptions()
                            .position(currentLoc)
                            .title(weather.data?.cityName)
                            .snippet("" + weather.data?.status + ", " + weather.data?.temperature + " °C," + " Humidity: " + weather.data?.humidity + ", Wind Speed: " + weather.data?.windSpeed)
                        map.addMarker(marker)
                    }
                    is Resource.Error -> {

                    }
                }
            }
        })
    }

    override fun onMapClick(point: LatLng) {
        if (lastSelectedMarker?.isInfoWindowShown == true) {
            lastSelectedMarker?.hideInfoWindow()
        } else {
            val marker = MarkerOptions()
                .position(point)
                .title(getString(R.string.loading))
                .snippet(getString(R.string.loading))
            map.addMarker(marker)
            map.moveCamera((CameraUpdateFactory.newLatLng(point)))
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        marker?.showInfoWindow()
        lastSelectedMarker = marker
        viewModel.getWeather(
            marker?.position?.latitude!!,
            marker.position?.longitude!!,
            CODE_CELSIUS_REQUEST
        ).observe(this, { weather ->
            if (weather != null) {
                when (weather) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        marker.title = weather.data?.cityName
                        marker.snippet =
                            "" + weather.data?.status + ", " + weather.data?.temperature + " °C," + " Humidity: " + weather.data?.humidity + ", Wind Speed: " + weather.data?.windSpeed
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
    }

    override fun onCameraIdle() {
        if (!::map.isInitialized) return
    }
}