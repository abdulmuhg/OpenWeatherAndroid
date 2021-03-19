package com.abdulmughni.personal.openweatherandroid.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.abdulmughni.personal.openweatherandroid.R
import com.abdulmughni.personal.openweatherandroid.core.data.Resource
import com.abdulmughni.personal.openweatherandroid.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var locationManager: LocationManager
    private var lat: Double = 0.0
    private var lng: Double = 0.0
    private var locationPermissionGranted: Boolean = false

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationManager =
            activity?.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        getLocationPermission()
        binding.switchUnits.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.getWeather(lat, lng, CODE_FAHRENHEIT_REQUEST)
                    .observe(viewLifecycleOwner, { weather ->
                        if (weather != null) {
                            when (weather) {
                                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE

                                is Resource.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    binding.status.text = weather.data?.status.toString()
                                    binding.cityName.text = weather.data?.cityName.toString()
                                    binding.textTemperature.text =
                                        weather.data?.temperature.toString()
                                    binding.textWind.text = weather.data?.windSpeed.toString()
                                    binding.textHumidity.text =
                                        getString(R.string.humidity) + weather.data?.humidity.toString()
                                    val iconUrl = weather.data?.icon.toString()
                                    Glide.with(requireContext())
                                        .load("https://openweathermap.org/img/wn/$iconUrl@2x.png")
                                        .into(binding.weatherIcon)

                                    binding.textSunrise.text = dateFormat(weather.data?.sunrise)
                                    binding.textSunset.text = dateFormat(weather.data?.sunset)

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
                Glide.with(requireContext())
                    .load(R.drawable.ic_fahrenheit)
                    .into(binding.temperatureUnits)
            } else {
                viewModel.getWeather(lat, lng, CODE_CELSIUS_REQUEST)
                    .observe(viewLifecycleOwner, { weather ->
                        if (weather != null) {
                            when (weather) {
                                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE

                                is Resource.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    binding.status.text = weather.data?.status.toString()
                                    binding.cityName.text = weather.data?.cityName.toString()
                                    binding.textTemperature.text =
                                        weather.data?.temperature.toString()
                                    binding.textWind.text = weather.data?.windSpeed.toString()
                                    binding.textHumidity.text =
                                        getString(R.string.humidity) + weather.data?.humidity.toString()
                                    val iconUrl = weather.data?.icon.toString()
                                    Glide.with(requireContext())
                                        .load("https://openweathermap.org/img/wn/$iconUrl@2x.png")
                                        .into(binding.weatherIcon)

                                    binding.textSunrise.text = dateFormat(weather.data?.sunrise)
                                    binding.textSunset.text = dateFormat(weather.data?.sunset)
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
                Glide.with(requireContext())
                    .load(R.drawable.ic_celsius)
                    .into(binding.temperatureUnits)
            }
        }
        binding.swipeRefresh.setOnRefreshListener {
            if (binding.switchUnits.isChecked) {
                viewModel.getWeather(lat, lng, CODE_FAHRENHEIT_REQUEST)
                    .observe(viewLifecycleOwner, { weather ->
                        if (weather != null) {
                            when (weather) {
                                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE

                                is Resource.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    binding.status.text = weather.data?.status.toString()
                                    binding.cityName.text = weather.data?.cityName.toString()
                                    binding.textTemperature.text =
                                        weather.data?.temperature.toString()
                                    binding.textWind.text = weather.data?.windSpeed.toString()
                                    binding.textHumidity.text =
                                        getString(R.string.humidity) + weather.data?.humidity.toString()
                                    val iconUrl = weather.data?.icon.toString()
                                    Glide.with(requireContext())
                                        .load("https://openweathermap.org/img/wn/$iconUrl@2x.png")
                                        .into(binding.weatherIcon)

                                    binding.textSunrise.text = dateFormat(weather.data?.sunrise)
                                    binding.textSunset.text = dateFormat(weather.data?.sunset)
                                    binding.swipeRefresh.isRefreshing = false
                                }
                                is Resource.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    binding.viewError.root.visibility = View.VISIBLE
                                    binding.viewError.tvError.text =
                                        weather.message ?: getString(R.string.something_wrong)
                                    binding.swipeRefresh.isRefreshing = false
                                }
                            }
                        }
                    })
            } else {
                viewModel.getWeather(lat, lng, CODE_CELSIUS_REQUEST)
                    .observe(viewLifecycleOwner, { weather ->
                        if (weather != null) {
                            when (weather) {
                                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE

                                is Resource.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    binding.status.text = weather.data?.status.toString()
                                    binding.cityName.text = weather.data?.cityName.toString()
                                    binding.textTemperature.text =
                                        weather.data?.temperature.toString()
                                    binding.textWind.text = weather.data?.windSpeed.toString()
                                    binding.textHumidity.text =
                                        getString(R.string.humidity) + weather.data?.humidity.toString()
                                    val iconUrl = weather.data?.icon.toString()
                                    Glide.with(requireContext())
                                        .load("https://openweathermap.org/img/wn/$iconUrl@2x.png")
                                        .into(binding.weatherIcon)

                                    binding.textSunrise.text = dateFormat(weather.data?.sunrise)
                                    binding.textSunset.text = dateFormat(weather.data?.sunset)
                                    binding.swipeRefresh.isRefreshing = false
                                }
                                is Resource.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    binding.viewError.root.visibility = View.VISIBLE
                                    binding.viewError.tvError.text =
                                        weather.message ?: getString(R.string.something_wrong)
                                    binding.swipeRefresh.isRefreshing = false
                                }
                            }
                        }
                    })
            }
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
            getDeviceCoordinate()
        } else {
            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    @SuppressLint("SetTextI18n")
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
                            binding.textWind.text = weather.data?.windSpeed.toString()
                            binding.textHumidity.text =
                                getString(R.string.humidity) + weather.data?.humidity.toString()
                            val iconUrl = weather.data?.icon.toString()
                            Glide.with(requireContext())
                                .load("https://openweathermap.org/img/wn/$iconUrl@2x.png")
                                .into(binding.weatherIcon)
                            binding.textSunrise.text = dateFormat(weather.data?.sunrise)
                            binding.textSunset.text = dateFormat(weather.data?.sunset)
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

    @SuppressLint("SimpleDateFormat")
    private fun dateFormat(timestamp: Int?): String {
        val time: String
        val unixTimestamp: Int? = timestamp
        val javaTimestamp = unixTimestamp?.times(1000L)
        return if (javaTimestamp != null) {
            Date(javaTimestamp)
            time = SimpleDateFormat("hh:mm").format(javaTimestamp)
            time
        } else {
            ""
        }
    }
}