package com.abdulmughni.personal.openweatherandroid.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.abdulmughni.personal.openweatherandroid.R
import com.abdulmughni.personal.openweatherandroid.core.data.Resource
import com.abdulmughni.personal.openweatherandroid.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var locationManager: LocationManager
    private var lat: Double = 0.0
    private var lng: Double = 0.0

    companion object {
        const val CODE_CELSIUS_REQUEST = "metric"
        const val CODE_FAHRENHEIT_REQUEST = "imperial"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationManager =
            activity?.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        getDeviceCoordinate()
    }

    private fun getDeviceCoordinate() {
        val criteria = Criteria()
        val provider: String? = locationManager.getBestProvider(criteria, false)
        var location: Location? = null
        if (provider != null) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            location = locationManager.getLastKnownLocation(provider)
        }
        if (location != null) {
            lat = location.latitude
            lng = location.longitude
        }

        viewModel.getWeather(lat, lng, CODE_CELSIUS_REQUEST)
            .observe(viewLifecycleOwner, { weather ->
                if (weather != null) {
                    when (weather) {
                        is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE

                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.status.text = weather.data?.status.toString()
                            binding.cityName.text = weather.data?.cityName.toString()
                            binding.textTemperature.text = weather.data?.temperature.toString()
                            binding.textHumidity.text = weather.data?.humidity.toString()
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