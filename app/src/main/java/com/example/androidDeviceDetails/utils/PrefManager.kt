@file:Suppress("DEPRECATION")

package com.example.androidDeviceDetails.utils

import android.content.Context
import android.preference.PreferenceManager

object PrefManager {
    fun initialLaunch(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val previouslyStarted = prefs.getBoolean("initialized", false)
        if (!previouslyStarted) {
            val edit = prefs.edit()
            edit.putBoolean("initialized", true)
            edit.apply()
            edit.commit()
            return true
        }
        return false
    }

}