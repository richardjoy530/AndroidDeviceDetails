package com.example.androidDeviceDetails.managers

import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.androidDeviceDetails.models.AppDataUsage
import com.example.androidDeviceDetails.models.DeviceDataUsage
import com.example.androidDeviceDetails.models.RoomDB
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
class AppDataUsageCollector(var context: Context) {
    private val firstInstallTime =
        context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime
    val db = RoomDB.getDatabase()!!
    private val networkStatsManager =
        context.getSystemService(AppCompatActivity.NETWORK_STATS_SERVICE) as NetworkStatsManager

    fun updateAppDataUsageDB() {
        val appDataUsageList = arrayListOf<AppDataUsage>()
        val networkStatsWifi = networkStatsManager.querySummary(
            NetworkCapabilities.TRANSPORT_WIFI,
            null, firstInstallTime, System.currentTimeMillis()
        )
        val networkStatsMobileData = networkStatsManager.querySummary(
            NetworkCapabilities.TRANSPORT_CELLULAR,
            null, firstInstallTime, System.currentTimeMillis()
        )
        val bucket = NetworkStats.Bucket()
        Log.d("TAG", "updateAppWifiDataUsageDB: ")
        while (networkStatsWifi.hasNextBucket() or networkStatsMobileData.hasNextBucket()) {
            if (networkStatsWifi.hasNextBucket()) {
                networkStatsWifi.getNextBucket(bucket)
                val packageName = context.packageManager.getNameForUid(bucket.uid)
                if (packageName != null && packageName != "null")
                    if (appDataUsageList.none { it.packageName == packageName })
                        appDataUsageList.add(appDataUsageFactory(bucket, true))
                    else
                        appDataUsageList.first {
                            it.packageName == packageName
                        }.apply {
                            receivedDataWifi += bucket.rxBytes
                            transferredDataWifi += bucket.txBytes
                        }
            }
            if (networkStatsMobileData.hasNextBucket()) {
                networkStatsMobileData.getNextBucket(bucket)
                val packageName = context.packageManager.getNameForUid(bucket.uid)
                if (packageName != null && packageName != "null")
                    if (appDataUsageList.none { it.packageName == packageName })
                        appDataUsageList.add(appDataUsageFactory(bucket, false))
                    else
                        appDataUsageList.first {
                            it.packageName == packageName
                        }.apply {
                            receivedDataMobile += bucket.rxBytes
                            transferredDataMobile += bucket.txBytes
                        }
            }
        }
        GlobalScope.launch { appDataUsageList.forEach { db.appDataUsage().insertAll(it) } }
    }

    fun updateDeviceDataUsageDB() {
        var totalWifiDataRx = 0L
        var totalWifiDataTx = 0L
        var totalMobileDataRx = 0L
        var totalMobileDataTx = 0L
        var bucket = networkStatsManager.querySummaryForDevice(
            NetworkCapabilities.TRANSPORT_WIFI,
            null, firstInstallTime, System.currentTimeMillis()
        )
        totalWifiDataRx += bucket.rxBytes
        totalWifiDataTx += bucket.txBytes
        bucket = networkStatsManager.querySummaryForDevice(
            NetworkCapabilities.TRANSPORT_CELLULAR,
            null, firstInstallTime, System.currentTimeMillis()
        )
        totalMobileDataRx += bucket.rxBytes
        totalMobileDataTx += bucket.txBytes
        GlobalScope.launch {
            db.deviceDataUsage().insertAll(
                DeviceDataUsage(
                    System.currentTimeMillis(),
                    totalWifiDataTx, totalMobileDataTx,
                    totalWifiDataRx, totalMobileDataRx
                )
            )
        }
    }

    private fun appDataUsageFactory(
        bucket: NetworkStats.Bucket,
        wifiEnable: Boolean = true
    ): AppDataUsage {
        val packageName = context.packageManager.getNameForUid(bucket.uid)!!
        return if (wifiEnable)
            AppDataUsage(
                System.currentTimeMillis(),
                packageName,
                bucket.txBytes, 0L,
                bucket.rxBytes, 0L
            )
        else
            AppDataUsage(
                System.currentTimeMillis(),
                packageName,
                0L, bucket.txBytes, 0L,
                bucket.rxBytes,
            )

    }

}