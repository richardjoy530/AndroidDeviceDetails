package com.example.analytics.base

import com.example.analytics.cooker.AppInfoCooker
import com.example.analytics.cooker.AppNetworkUsageCooker
import com.example.analytics.cooker.BatteryCooker
import com.example.analytics.cooker.MainActivityCooker
import com.example.analytics.interfaces.ICookingDone
import com.example.analytics.models.TimePeriod
import com.example.analytics.ui.AppInfoActivity
import com.example.analytics.ui.BatteryActivity
import com.example.analytics.ui.MainActivity
import com.example.analytics.ui.NetworkUsageActivity

abstract class BaseCooker {
    abstract fun <T> cook(time: TimePeriod, callback: ICookingDone<T>)

    companion object {
        fun getCooker(type: String): BaseCooker? {
            return when (type) {
                MainActivity.NAME -> MainActivityCooker()
                BatteryActivity.NAME -> BatteryCooker()
                AppInfoActivity.NAME -> AppInfoCooker()
                NetworkUsageActivity.NAME -> AppNetworkUsageCooker()
                else -> null
            }
        }
    }
}