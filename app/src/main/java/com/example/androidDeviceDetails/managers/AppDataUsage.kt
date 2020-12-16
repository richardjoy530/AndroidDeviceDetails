package com.example.androidDeviceDetails.managers

import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.RemoteException
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class AppDataUsage(var context: Context){
    @RequiresApi(Build.VERSION_CODES.M)
    val networkStatsManager = context.getSystemService(AppCompatActivity.NETWORK_STATS_SERVICE) as NetworkStatsManager

    @RequiresApi(Build.VERSION_CODES.M)
    fun updateAppDataUsageDB(startTime: Long, endTime: Long):Long{
        //val uid = getUid("com.google.android.youtube")
        val networkStatsManager = context.getSystemService(AppCompatActivity.NETWORK_STATS_SERVICE) as NetworkStatsManager
        val networkStats: NetworkStats
        var totalUsage = 0L
        try {
            networkStats = networkStatsManager.querySummary(
                ConnectivityManager.TYPE_WIFI,
                "",
                startTime,
                endTime
            )

            val bucket = NetworkStats.Bucket()
            while (networkStats.hasNextBucket()) {

                networkStats.getNextBucket(bucket)
                //Log.d("TAG", "bucket:${packageManager.getNameForUid(bucket.uid)} " )

                if (bucket.uid == uid) {
                    totalUsage += bucket.txBytes + bucket.rxBytes
                }
            }
        } catch (e: RemoteException) {
            Log.d("TAG", "getUsage: RemoteException")
        }
        Log.d("TAG", "getUsage: " + getFileSize(totalUsage))
        return totalUsage
    }
}