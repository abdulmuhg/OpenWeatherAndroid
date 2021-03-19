package com.abdulmughni.personal.openweatherandroid.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.abdulmughni.personal.openweatherandroid.R
import com.abdulmughni.personal.openweatherandroid.core.data.Resource
import com.abdulmughni.personal.openweatherandroid.core.ui.MapsActivity
import com.abdulmughni.personal.openweatherandroid.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var locationManager: LocationManager
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var locationPermissionGranted: Boolean = false

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        getLocationPermission()

        binding.btnMapActivity.setOnClickListener {
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
            getDeviceCoordinate()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
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

            val rounded = lat.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()

            val dec = DecimalFormat("000.000")
            var format: String = dec.format(lat)
        }

        Toast.makeText(this, "Longitude : $lng Latitude : $lat", Toast.LENGTH_SHORT).show()

        viewModel.getWeatherGPS(lat, lng).observe(this@HomeActivity, { weather ->
            if (weather != null) {
                when (weather) {
                    is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.status.text = weather.data?.cityName.toString()
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewError.root.visibility = View.VISIBLE
                        binding.viewError.tvError.text =
                            weather.message ?: getString(R.string.something_wrong)
                    }
                }
            }
        })
    }


}