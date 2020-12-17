package com.example.androidDeviceDetails.managers

import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.NetworkCapabilities
import android.os.Build
import android.os.RemoteException
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.androidDeviceDetails.models.AppDataUsage
import com.example.androidDeviceDetails.models.RoomDB
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
class AppDataUsageCollector(var context: Context) {
    private val networkStatsManager =
        context.getSystemService(AppCompatActivity.NETWORK_STATS_SERVICE) as NetworkStatsManager

    fun updateAppWifiDataUsageDB(minutesAgo: Long) {
        val appDataUsageList = arrayListOf<AppDataUsage>()
        val db = RoomDB.getDatabase()!!
        val networkStatsWifi: NetworkStats
        val networkStatsMobileData: NetworkStats

        try {
            networkStatsWifi = networkStatsManager.querySummary(
                NetworkCapabilities.TRANSPORT_WIFI,
                null,
                context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime,
                System.currentTimeMillis()
            )
            networkStatsMobileData = networkStatsManager.querySummary(
                NetworkCapabilities.TRANSPORT_CELLULAR,
                null,
                context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime,
                System.currentTimeMillis()
            )
            val bucket = NetworkStats.Bucket()
            Log.d("TAG", "updateAppWifiDataUsageDB: ")
            while (networkStatsWifi.hasNextBucket()) {
                networkStatsWifi.getNextBucket(bucket)
                val packageName = context.packageManager.getNameForUid(bucket.uid)
                if (packageName != null && packageName != "null") {
                    if (appDataUsageList.none {
                            it.packageName == packageName
                        }) {
                        appDataUsageList.add(
                            AppDataUsage(
                                System.currentTimeMillis(),
                                packageName,
                                bucket.txBytes, 0L,
                                bucket.rxBytes, 0L
                            )
                        )
                    } else {
                        appDataUsageList.first {
                            it.packageName == packageName
                        }.receivedDataWifi += bucket.rxBytes
                        appDataUsageList.first {
                            it.packageName == packageName
                        }.transferredDataWifi += bucket.txBytes
                    }

                }

            }
            while (networkStatsMobileData.hasNextBucket()) {
                networkStatsMobileData.getNextBucket(bucket)
                val packageName = context.packageManager.getNameForUid(bucket.uid)
                if (packageName != null && packageName != "null") {
                    if (appDataUsageList.none {
                            it.packageName == packageName
                        }) {
                        appDataUsageList.add(
                            AppDataUsage(
                                System.currentTimeMillis(),
                                packageName,
                                0L, bucket.txBytes, 0L,
                                bucket.rxBytes,
                            )
                        )
                    } else {
                        appDataUsageList.first {
                            it.packageName == packageName
                        }.receivedDataMobile += bucket.rxBytes
                        appDataUsageList.first {
                            it.packageName == packageName
                        }.transferredDataMobile += bucket.txBytes
                    }

                }

            }
            GlobalScope.launch {
                appDataUsageList.forEach { db.appDataUsage().insertAll(it) }
            }

        } catch (e: RemoteException) {
            Log.d("TAG", "getUsage: RemoteException")
        }

    }


}