package com.example.analytics.cooker

import com.example.analytics.base.BaseCooker
import com.example.analytics.interfaces.ICookingDone
import com.example.analytics.models.TimePeriod
import com.example.analytics.models.appInfo.AppInfoCookedData
import com.example.analytics.models.appInfo.EventType
import com.example.analytics.models.database.AppHistoryDao
import com.example.analytics.models.database.RoomDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class AppInfoCooker : BaseCooker() {

    /**
     * Cook data for App Info from the collected data available in the [AppHistoryDao] database for
     * the requested time interval.
     * >
     * Overrides : [cook] in [BaseCooker]
     * @param time data class object that contains start time and end time.
     * @param callback A callback that accepts the cooked list once cooking is done
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T> cook(time: TimePeriod, callback: ICookingDone<T>) {
        GlobalScope.launch(Dispatchers.IO) {
            val db = RoomDB.getDatabase()!!
            val startTime = time.startTime
            val endTime = time.endTime
            val appList = arrayListOf<AppInfoCookedData>()
            val ids = db.appHistoryDao().getIdsBetween(startTime, endTime)
            for (id in ids) {
                val lastRecord = db.appHistoryDao().getLatestRecordBetween(id, startTime, endTime)
                val initialRecord =
                    db.appHistoryDao().getInitialRecordBetween(id, startTime, endTime)

                @Suppress("CascadeIf")
                var evt: AppInfoCookedData? = null
                if (lastRecord.eventType == EventType.APP_ENROLL.ordinal) {
                    appList.add(
                        AppInfoCookedData(
                            lastRecord.appTitle,
                            EventType.APP_ENROLL,
                            lastRecord.currentVersionCode,
                            lastRecord.appId,
                            lastRecord.isSystemApp
                        )
                    )
                    continue
                } else if (lastRecord.eventType == EventType.APP_UNINSTALLED.ordinal) {
                    appList.add(
                        AppInfoCookedData(
                            lastRecord.appTitle,
                            EventType.APP_UNINSTALLED,
                            lastRecord.previousVersionCode,
                            lastRecord.appId,
                            lastRecord.isSystemApp
                        )
                    )
                    continue
                } else if (initialRecord.previousVersionCode != lastRecord.currentVersionCode) {
                    evt = AppInfoCookedData(
                        lastRecord.appTitle,
                        EventType.APP_UPDATED,
                        lastRecord.currentVersionCode,
                        lastRecord.appId,
                        lastRecord.isSystemApp
                    )
                }
                if (initialRecord.previousVersionCode == 0L) {
                    evt = AppInfoCookedData(
                        lastRecord.appTitle,
                        EventType.APP_INSTALLED,
                        lastRecord.currentVersionCode,
                        lastRecord.appId,
                        lastRecord.isSystemApp
                    )
                }
                if (evt != null) {
                    appList.add(evt)
                }
            }

            if (appList.isEmpty())
                callback.onDone(arrayListOf())
            else {
                for (app in appList) {
                    app.packageName = db.appsDao().getPackageByID(app.appId)
                }
                callback.onDone(appList as ArrayList<T>)
            }
        }
    }
}