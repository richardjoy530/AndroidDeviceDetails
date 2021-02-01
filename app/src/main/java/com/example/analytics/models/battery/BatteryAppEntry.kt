package com.example.analytics.models.battery

import com.example.analytics.adapters.BatteryListAdapter

/**
 * A data class used by the list view adaptor for displaying in the [BatteryListAdapter]
 */
data class BatteryAppEntry(var packageId: String, var drop: Int = 0)