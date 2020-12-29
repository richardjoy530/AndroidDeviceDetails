package com.example.androidDeviceDetails.base

import android.content.Context
import com.example.androidDeviceDetails.activities.BatteryActivity
import com.example.androidDeviceDetails.databinding.ActivityBatteryBinding
import com.example.androidDeviceDetails.viewModel.BatteryViewModel

abstract class BaseViewModel {
    abstract fun onNoData()
    abstract fun <T> onData(outputList: ArrayList<T>)

    companion object {
        fun getViewModel(
            type: String,
            binding: Any?,
            context: Context
        ): BaseViewModel {
            return if (BatteryActivity.NAME == type) {
                BatteryViewModel(binding as ActivityBatteryBinding, context)
            } else BatteryViewModel(binding as ActivityBatteryBinding, context)

        }
    }
}