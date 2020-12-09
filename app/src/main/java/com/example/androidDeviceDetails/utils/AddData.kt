package com.example.androidDeviceDetails.utils

import android.content.Context
import com.example.androidDeviceDetails.models.AppDetails
import com.example.androidDeviceDetails.models.EventType
import com.example.androidDeviceDetails.models.RoomDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object AddData {

    fun appInstalled(context: Context, packageName: String) {
        val newVersionDetails = Utils.getVersion(context, packageName)
        val db = RoomDB.getDatabase()!!
        GlobalScope.launch(Dispatchers.IO) {
            var id = db.appsDao().getIdByName(packageName)
            if (id == 0) {
                DbHelper.writeToAppsDb(0, packageName, db)
                id = db.appsDao().getIdByName(packageName)
                DbHelper.writeToAppHistoryDb(
                    id,
                    EventType.APP_INSTALLED.ordinal,
                    newVersionDetails,
                    db
                )
            } else {
                val currentAppHistory = db.appHistoryDao().getLastRecord(id)
                val event = if (currentAppHistory.versionCode!! >= newVersionDetails.versionCode) {
                    EventType.APP_INSTALLED.ordinal
                } else {
                    EventType.APP_UPDATED.ordinal
                }
                DbHelper.writeToAppHistoryDb(
                    id,
                    event,
                    newVersionDetails,
                    db
                )
            }
        }
    }

    fun appUninstalled(context: Context, packageName: String) {
        val db = RoomDB.getDatabase()!!
        GlobalScope.launch(Dispatchers.IO) {
            val id = db.appsDao().getIdByName(packageName)
            val currentAppHistory = db.appHistoryDao().getLastRecord(id)
            val appDetails =
                currentAppHistory.versionName?.let {
                    currentAppHistory.versionCode?.let { it1 ->
                        currentAppHistory.appSize?.let { it2 ->
                            AppDetails(
                                it1, it, it2
                            )
                        }
                    }
                }
            if (appDetails != null) {
                DbHelper.writeToAppHistoryDb(id, EventType.APP_UNINSTALLED.ordinal, appDetails, db)
            }
        }
    }

    fun appUpgraded(context: Context, packageName: String) {
        val db = RoomDB.getDatabase()!!
        GlobalScope.launch(Dispatchers.IO) {
            val newVersionDetails = Utils.getVersion(context, packageName)
            val id = db.appsDao().getIdByName(packageName)
            val currentAppHistory = db.appHistoryDao().getLastRecord(id)
            if (currentAppHistory.versionCode!! < newVersionDetails.versionCode)
                DbHelper.writeToAppHistoryDb(
                    id,
                    EventType.APP_UPDATED.ordinal,
                    newVersionDetails,
                    db
                )
        }
    }

}