package com.example.androidDeviceDetails.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.androidDeviceDetails.models.AppUsageModel
import com.example.androidDeviceDetails.models.RoomDB
import com.example.androidDeviceDetails.receivers.BatteryReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

const val CHANNEL_ID = "androidDeviceDetails"

class CollectorService : Service() {

    private lateinit var mReceiver: BroadcastReceiver
    private lateinit var timer: Timer

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        mReceiver = BatteryReceiver()
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Collecting Data",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
            val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("").build()
            startForeground(1, notification)

        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        this.registerReceiver(mReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        timer = Timer()
        val timeInterval: Long = 1
        val appUsage = AppUsage(this)
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    appUsage.updateAppUsageDB(timeInterval)
                }
            },
            0, 1000 * 60 * timeInterval
        )
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(mReceiver)
        timer.cancel()
        stopSelf()
    }
}

class AppUsage(context: Context) {

    private var usageStatsManager: UsageStatsManager =
        context.getSystemService(AppCompatActivity.USAGE_STATS_SERVICE) as UsageStatsManager

    fun updateAppUsageDB(minutesAgo: Long) {
        val db = RoomDB.getDatabase()!!
        val events = usageStatsManager.queryEvents(
            System.currentTimeMillis() - minutesAgo * 60 * 1000,
            System.currentTimeMillis()
        )
        while (events.hasNextEvent()) {
            val evt = UsageEvents.Event()
            events.getNextEvent(evt)
            if (evt.eventType == 1) {
                val appUsageData = AppUsageModel(
                    timeStamp = evt.timeStamp,
                    packageName = evt.packageName
                )
                GlobalScope.launch(Dispatchers.IO) { db.appUsageInfoDao().insertAll(appUsageData) }
            }
        }
    }
}