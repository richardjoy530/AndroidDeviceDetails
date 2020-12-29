package com.example.androidDeviceDetails.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.androidDeviceDetails.utils.AppInfoCollectionHelper

class AppStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val packageName = intent.data?.schemeSpecificPart ?: "not found"

        if (action == Intent.ACTION_PACKAGE_ADDED) {
            if (!intent.getBooleanExtra(Intent.EXTRA_REPLACING, false))
                AppInfoCollectionHelper.appInstalled(context, packageName)
            else
                AppInfoCollectionHelper.appUpgraded(context, packageName)
        }

        if (action == Intent.ACTION_PACKAGE_FULLY_REMOVED) {
            AppInfoCollectionHelper.appUninstalled(context, packageName)
        }
    }

}

