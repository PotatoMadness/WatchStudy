package com.example.testevinfra

import android.location.Location
import android.location.LocationManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.testevinfra.databinding.ItemStationInfoBinding
import kotlin.math.roundToInt

class StationAdapter(var currentLocation: Location?, var list: List<StationInfo>): RecyclerView.Adapter<StationAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemStationInfoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemStationInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stationInfo = list[position]
        with(holder.binding) {
            tvSnm.text = stationInfo.snm
            tvDistance.text = "${getDistance(stationInfo.lat, stationInfo.lon).toString()} km"
            tvOperator.text = stationInfo.op

            val map = stationInfo.cl.groupBy { it.p }.toSortedMap(Comparator.reverseOrder())
            val types = map.keys.toList()
            val values = map.values.toList()
            val firstType = types[0]
            val enableCnt = values[0].count{ it.cst == "3" }
            tvFirstSpeed.text = if (firstType.toInt() < 50) "완속" else firstType
            tvFirstStatus.text = "${enableCnt}/${values[0].size}대"
            if (map.size > 1) {
                val secondType = types[1]
                val enableCnt = values[0].count{ it.cst == "3" }
                llSpeed2.isVisible = true
                tvFirstSpeed.text = if (secondType.toInt() < 50) "완속" else secondType
                tvFirstStatus.text = "${enableCnt}/${values[1].size}대"
            } else {
                llSpeed2.isVisible = false
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun getDistance(lat: String, lng: String) : Float?{
        return try {
            if (currentLocation == null) return throw Exception("현재위치 없음")
            val targetLoc = Location(LocationManager.NETWORK_PROVIDER)
            targetLoc.latitude = lat.toDouble()
            targetLoc.longitude = lng.toDouble()
            val dist = currentLocation!!.distanceTo(targetLoc) / 1000
            dist.roundToInt().toFloat()
        } catch (e: Exception) {
            null
        }
    }
}