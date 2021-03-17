package com.abdulmughni.personal.openweatherandroid.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.abdulmughni.personal.openweatherandroid.R
import com.abdulmughni.personal.openweatherandroid.core.data.Resource
import com.abdulmughni.personal.openweatherandroid.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getWeatherGPS(12, 100).observe(this, { weather ->
            if (weather != null){
                when(weather){
                    is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.status.text = weather.data?.cityName.toString()
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.viewError.root.visibility = View.VISIBLE
                        binding.viewError.tvError.text = weather.message ?: getString(R.string.something_wrong)
                    }
                }
            }
        })

//        viewModel.weather.observe(this, { weather ->
//            if (weather != null){
//                when(weather){
//                    is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
//                    is Resource.Success -> {
//                        binding.progressBar.visibility = View.GONE
//                        binding.status.text = weather.data?.cityName.toString()
//                    }
//                    is Resource.Error -> {
//                        binding.progressBar.visibility = View.GONE
//                        binding.viewError.root.visibility = View.VISIBLE
//                        binding.viewError.tvError.text = weather.message ?: getString(R.string.something_wrong)
//                    }
//                }
//            }
//        })
    }
}