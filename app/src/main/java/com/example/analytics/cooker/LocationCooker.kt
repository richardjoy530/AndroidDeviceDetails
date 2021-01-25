package com.example.analytics.cooker

import android.location.Geocoder
import android.util.Log
import com.example.analytics.DeviceDetailsApplication
import com.example.analytics.base.BaseCooker
import com.example.analytics.interfaces.ICookingDone
import com.example.analytics.models.TimePeriod
import com.example.analytics.models.database.RoomDB
import com.example.analytics.models.location.LocationDisplayModel
import com.example.analytics.models.location.LocationModel
import com.github.davidmoten.geo.GeoHash
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LocationCooker : BaseCooker() {
    private val geoHashLength: Int = 8
    private var locationDatabase: RoomDB = RoomDB.getDatabase()!!
    private val geoCoder: Geocoder = Geocoder(DeviceDetailsApplication.instance)


    private fun cookData(locationList: ArrayList<LocationModel>): ArrayList<LocationDisplayModel> {
        val cookedLocationList = emptyList<String>().toMutableList()
        var prevLocationHash = ""
        for (location in locationList) {
            val newHash =
                GeoHash.encodeHash(location.latitude!!, location.longitude!!, geoHashLength).toString()
            if (newHash != prevLocationHash) {
                prevLocationHash = newHash
                cookedLocationList.add(newHash)
            }
        }
        val countedData = cookedLocationList.groupingBy { it }.eachCount()
        val locationDisplayModel: ArrayList<LocationDisplayModel> = ArrayList()
        for ((k,v) in countedData){
            val latLong = GeoHash.decodeHash(k)
            val address = geoCoder.getFromLocation(latLong.lat, latLong.lon, 1)[0]?.locality?.toString()
            locationDisplayModel.add(LocationDisplayModel(k,v,address ?: "cannot locate"))
        }
        Log.d("CountedData", "cookDataCount: ${cookedLocationList.size} ")
        return locationDisplayModel
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> cook(time: TimePeriod, callback: ICookingDone<T>) {
        GlobalScope.launch {
            val res = locationDatabase.locationDao()
                .readDataFromDate(time.startTime, time.endTime) as ArrayList<LocationModel>
            Log.d("LocationData", "loadData: $res")
            callback.onDone(cookData(res) as ArrayList<T>)
        }
    }

}