package com.example.androidDeviceDetails

import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.androidDeviceDetails.models.AppDataUsage
import com.example.androidDeviceDetails.models.RoomDB
import com.example.androidDeviceDetails.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

const val TAG = "Main"
const val timeIntervalSec: Long = 60
val appWifiUsageList = arrayListOf<AppWifiUsage>()
var fetches = 0

fun statsTest() {

    val timer = Timer()
    val context = DeviceDetailsApplication.instance
    val networkStatsManager =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(AppCompatActivity.NETWORK_STATS_SERVICE) as NetworkStatsManager
        } else {
            TODO("VERSION.SDK_INT < M")
        }

    timer.scheduleAtFixedRate(
        object : TimerTask() {
            override fun run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    fetches += 1
                    runner(context, networkStatsManager)
                }
            }
        },
        0, 1000 * timeIntervalSec
    )
}

@RequiresApi(Build.VERSION_CODES.M)
fun runner(context: Context, networkStatsManager: NetworkStatsManager) {
    GlobalScope.launch(Dispatchers.IO) {
        val networkStats = networkStatsManager.querySummary(
            NetworkCapabilities.TRANSPORT_WIFI,
            "",
            System.currentTimeMillis() - timeIntervalSec * 1000,
            System.currentTimeMillis()
        )
        val bucket = NetworkStats.Bucket()
        while (networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket)
            val packageName = context.packageManager.getNameForUid(bucket.uid)
            if (packageName != null && packageName == "com.google.android.youtube") {
                appWifiUsageList.add(
                    AppWifiUsage(
                        fetches, (bucket.rxBytes + bucket.txBytes) / (1024 * 1024),
                        Utils.getApplicationLabel(packageName),
                        bucket
                    )
                )
                RoomDB.getDatabase()!!.appDataUsage().insertAll(
                    AppDataUsage(
                        System.currentTimeMillis(),
                        Utils.getApplicationLabel(packageName),
                        (bucket.rxBytes + bucket.txBytes) / (1024 * 1024)
                    )
                )
//            Log.d(
//                TAG,
//                "Usage: ${Utils.getApplicationLabel(packageName)} : ${(bucket.txBytes + bucket.rxBytes) / (1024 * 1024)}MB "
//            )
//            Log.d(TAG, "---------")
            }
        }
        networkStats.close()
    }
//    Log.d(TAG, "runner: count = ${appWifiUsageList.size}")
}

data class AppWifiUsage(
    var fetches: Int,
    var dataUsageMB: Long,
    var packageName: String,
    var bucket: NetworkStats.Bucket
)