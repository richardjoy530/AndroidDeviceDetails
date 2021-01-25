package com.example.analytics.base

import com.example.analytics.cooker.*
import com.example.analytics.interfaces.ICookingDone
import com.example.analytics.models.TimePeriod
import com.example.analytics.ui.*

abstract class BaseCooker {
    abstract fun <T> cook(time: TimePeriod, callback: ICookingDone<T>)

    companion object {
        fun getCooker(type: String): BaseCooker? {
            return when (type) {
                MainActivity.NAME->MainActivityCooker()
                BatteryActivity.NAME -> BatteryCooker()
                AppInfoActivity.NAME -> AppInfoCooker()
                NetworkUsageActivity.NAME -> AppNetworkUsageCooker()
                SignalActivity.NAME -> SignalCooker()
                LocationActivity.NAME -> LocationCooker()
                else -> null
            }
        }
    }
}