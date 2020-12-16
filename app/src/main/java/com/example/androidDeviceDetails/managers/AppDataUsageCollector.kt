package com.example.androidDeviceDetails.managers

import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.RemoteException
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.androidDeviceDetails.models.AppDataUsage
import com.example.androidDeviceDetails.models.RoomDB
import com.example.androidDeviceDetails.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
class AppDataUsageCollector(var context: Context) {
    private val networkStatsManager =
        context.getSystemService(AppCompatActivity.NETWORK_STATS_SERVICE) as NetworkStatsManager

    fun updateAppDataUsageDB(minutesAgo: Long) {
        val db = RoomDB.getDatabase()!!
        val networkStats: NetworkStats
        try {
            networkStats = networkStatsManager.querySummary(
                ConnectivityManager.TYPE_WIFI,
                "",
                context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime,
                System.currentTimeMillis()
            )
            val bucket = NetworkStats.Bucket()
            while (networkStats.hasNextBucket()) {
                networkStats.getNextBucket(bucket)
                val packageName = context.packageManager.getNameForUid(bucket.uid)
                if (packageName!= null && packageName != "null") {
                    try {
                        Log.d(
                            "TAG",
                            "updateAppDataUsageDB: ${Utils.getApplicationLabel(packageName)} "
                        )
                    } catch (e: PackageManager.NameNotFoundException) {
                        Log.e("Error", "PackageName null")
                    }
                    val appDataUsage = AppDataUsage(
                        System.currentTimeMillis(),
                        packageName,
                        bucket.txBytes + bucket.rxBytes
                    )
                    Log.d("TAG", "Usage: ${(bucket.txBytes + bucket.rxBytes) / (1024 * 1024)}MB ")
                    Log.d("TAG", "updateAppDataUsageDB: ")
                    GlobalScope.launch(Dispatchers.IO) { db.appDataUsage().insertAll(appDataUsage) }
                }
            }
        } catch (e: RemoteException) {
            Log.d("TAG", "getUsage: RemoteException")
        }

    }
}