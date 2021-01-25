package com.example.analytics.base

import android.content.Context
import androidx.core.view.isVisible
import com.example.analytics.databinding.*
import com.example.analytics.ui.*
import com.example.analytics.viewModel.*
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseViewModel {
    abstract fun <T> onDone(outputList: ArrayList<T>)

    companion object {
        fun getViewModel(type: String, binding: Any?, context: Context): BaseViewModel? {
            return when (type) {
                MainActivity.NAME -> MainActivityViewModel(binding as ActivityMainBinding, context)
                BatteryActivity.NAME -> BatteryViewModel(binding as ActivityBatteryBinding, context)
                AppInfoActivity.NAME -> AppInfoViewModel(binding as ActivityAppInfoBinding, context)
                SignalActivity.NAME -> SignalViewModel(binding as ActivitySignalBinding, context)
                LocationActivity.NAME -> LocationViewModel(
                    binding as ActivityLocationBinding, context
                )
                NetworkUsageActivity.NAME -> NetworkUsageViewModel(
                    binding as ActivityAppDataBinding, context
                )
                else -> null
            }
        }
    }

    fun updateDateTimeUI(
        startCalendar: Calendar, endCalendar: Calendar, binding: DateTimePickerBinding
    ) {
        val simpleDateFormat = SimpleDateFormat("hh:mm", Locale.ENGLISH)
        binding.apply {
            startTime.text = simpleDateFormat.format(Date(startCalendar.timeInMillis))
            endTime.text = simpleDateFormat.format(Date(endCalendar.timeInMillis))

            simpleDateFormat.applyPattern("dd, MMM yyyy")
            startDate.text = simpleDateFormat.format(Date(startCalendar.timeInMillis))
            endDate.text = simpleDateFormat.format(Date(endCalendar.timeInMillis))

            simpleDateFormat.applyPattern("a")
            startAMPM.text = simpleDateFormat.format(Date(startCalendar.timeInMillis))
                .toLowerCase(Locale.ENGLISH)
            endAMPM.text =
                simpleDateFormat.format(Date(endCalendar.timeInMillis)).toLowerCase(Locale.ENGLISH)
        }
    }

    open fun isLoading(dateTimePickerBinding: DateTimePickerBinding, enable: Boolean) {
        dateTimePickerBinding.root.post { dateTimePickerBinding.progressBar.isVisible = enable }
    }

    open fun filter(type: Int) {}

    open fun sort(type: Int) {}
}