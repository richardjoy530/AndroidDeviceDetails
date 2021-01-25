package com.example.analytics.collectors

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.example.analytics.base.BaseCollector
import com.example.analytics.utils.AppInfoCollectionHelper

/**
 * Implements [BaseCollector]
 * An event based collector which collects the App Install, Uninstall and Update information.
 */
class AppInfoCollector(var context: Context) : BaseCollector() {

    object AppInfoReceiver : BroadcastReceiver() {
        /**
         * Receiver which gets notified when an App event is occurred.
         * Broadcast Action: Sent when an app is install, updated or uninstalled.
         *
         * It has :
         * [android.content.Intent.ACTION_PACKAGE_ADDED],
         * [android.content.Intent.EXTRA_REPLACING],
         * [android.content.Intent.ACTION_PACKAGE_FULLY_REMOVED]
         *
         */
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val packageName = intent.data?.schemeSpecificPart ?: "not found"

            if (action == Intent.ACTION_PACKAGE_ADDED) {
                if (!intent.getBooleanExtra(Intent.EXTRA_REPLACING, false))
                    AppInfoCollectionHelper.appInstalled(context, packageName)
                else
                    AppInfoCollectionHelper.appUpgraded(context, packageName)
            } else if (action == Intent.ACTION_PACKAGE_FULLY_REMOVED) {
                AppInfoCollectionHelper.appUninstalled(packageName)
            }
        }
    }

    /**
     * Registers the [AppInfoReceiver]
     */
    override fun start() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_PACKAGE_ADDED)
        filter.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED)
        filter.addDataScheme("package")
        context.registerReceiver(AppInfoReceiver, filter)
    }

    /**
     * Unregisters the [AppInfoReceiver]
     */
    override fun stop() = context.unregisterReceiver(AppInfoReceiver)

}

