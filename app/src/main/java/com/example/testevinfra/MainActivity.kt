package com.example.testevinfra

import android.Manifest
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.testevinfra.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.vm = viewModel
        setContentView(binding.root)

        initView()
        initObserver()
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            666
        )
    }

    private fun initView() {
        binding.layoutMain.setOnClickListener{
            this@MainActivity.supportFragmentManager.beginTransaction()
                .replace(binding.layoutStation.id, StationInfoFragment())
                .commitAllowingStateLoss()
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.lastLocation.filterNotNull().collect {
                        if (viewModel.stationList.value == null) {
                            viewModel.getStationInfo(it)
                        }
                        val gCoder = Geocoder(this@MainActivity, Locale.KOREA)
                        val addr = gCoder.getFromLocation(it.latitude, it.longitude, 1) ?: return@collect
                        binding.tvLoc.text = addr[0].thoroughfare
                    }
                }
                launch {
                    viewModel.stationList.collect {
                        when (it) {
                            is ResultStationInfo.IsLoading -> {
                                // loading animation
                            }
                            is ResultStationInfo.Success -> {
                                val cnt = it.result.list.count { station ->
                                    station.cl.any { charger ->
                                        charger.cst == "3"
                                    }
                                }
                                binding.tvEnableCount.text = "${cnt}곳 충전가능"
                                binding.tvTimestamp.text = viewModel.timestamp.value
                            }
                        }
                    }
                }
            }
        }
    }

}
