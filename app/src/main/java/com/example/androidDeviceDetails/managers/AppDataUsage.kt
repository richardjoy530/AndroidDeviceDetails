package com.example.androidDeviceDetails.managers

import android.app.usage.NetworkStatsManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class AppDataUsage(context: Context){
    @RequiresApi(Build.VERSION_CODES.M)
    val networkStatsManager = context.getSystemService(AppCompatActivity.NETWORK_STATS_SERVICE) as NetworkStatsManager

    fun updateApp
}