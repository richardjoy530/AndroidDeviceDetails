package com.example.androidDeviceDetails

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.androidDeviceDetails.utils.AddData


class UninstallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val packageName = intent.data?.schemeSpecificPart ?: "not found"

        if (action == Intent.ACTION_PACKAGE_ADDED && !intent.getBooleanExtra(
                Intent.EXTRA_REPLACING, false
            )
        ) {
            AddData.appInstalled(context, packageName)
        }

        if (action == Intent.ACTION_PACKAGE_FULLY_REMOVED) {
            AddData.appUninstalled(context, packageName)
        }

        if (action == Intent.ACTION_PACKAGE_REPLACED) {
            AddData.appUpgraded(context, packageName)
        }
    }
}

