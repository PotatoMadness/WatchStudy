package com.example.testevinfra

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.testevinfra.databinding.FragmentStationInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StationInfoFragment : Fragment(){
    private lateinit var viewModel:MainViewModel
    private lateinit var binding: FragmentStationInfoBinding
    private lateinit var adapter: StationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStationInfoBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
        initView()
    }

    private fun initView() {
        if (viewModel.stationList.value is ResultStationInfo.Success) {
            val list = (viewModel.stationList.value as ResultStationInfo.Success).result.list
            adapter = StationAdapter(viewModel.lastLocation.value, list)
            binding.vpStation.adapter = adapter
        } else throw Exception("list 없음")
    }
}