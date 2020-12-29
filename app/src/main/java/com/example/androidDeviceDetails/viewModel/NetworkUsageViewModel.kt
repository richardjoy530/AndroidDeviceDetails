package com.example.androidDeviceDetails.viewModel

import android.content.Context
import androidx.core.view.isVisible
import com.example.androidDeviceDetails.R
import com.example.androidDeviceDetails.adapters.NetWorkUsageListAdapter
import com.example.androidDeviceDetails.databinding.ActivityAppDataBinding
import com.example.androidDeviceDetails.models.networkUsageModels.AppNetworkUsageEntity
import com.example.androidDeviceDetails.utils.Utils
import java.text.DecimalFormat
import java.util.*

class NetworkUsageViewModel(
    val context: Context,
    private val networkUsageBinding: ActivityAppDataBinding
) {
    fun updateTextViews(startCalendar: Calendar, endCalendar: Calendar) {
        val dec = DecimalFormat("00")

        var startTime = dec.format(startCalendar.get(Calendar.HOUR)) + ":"
        startTime += dec.format(startCalendar.get(Calendar.MINUTE))

        var endTime = dec.format(endCalendar.get(Calendar.HOUR)) + ":"
        endTime += dec.format(endCalendar.get(Calendar.MINUTE))

        var startDate = startCalendar.get(Calendar.DAY_OF_MONTH).toString() + ", "
        startDate += Utils.getMonth(startCalendar.get(Calendar.MONTH)) + " "
        startDate += startCalendar.get(Calendar.YEAR)

        var endDate = endCalendar.get(Calendar.DAY_OF_MONTH).toString() + ", "
        endDate += Utils.getMonth(endCalendar.get(Calendar.MONTH)) + " "
        endDate += endCalendar.get(Calendar.YEAR)

        networkUsageBinding.apply {
            this.startTime.text = startTime
            this.startDate.text = startDate
            this.endTime.text = endTime
            this.endDate.text = endDate
            this.startAMPM.text = if (startCalendar.get(Calendar.AM_PM) == 0) "am" else "pm"
        }
    }

    fun onNoData() {
        networkUsageBinding.root.post {
            networkUsageBinding.apply {
                appDataListView.adapter = NetWorkUsageListAdapter(
                    context,
                    R.layout.appdata_tile,
                    arrayListOf()
                )
                noData.isVisible = true
            }
        }
    }

    fun onData(outputList: ArrayList<AppNetworkUsageEntity>) {
        networkUsageBinding.root.post {
            networkUsageBinding.apply {
                appDataListView.adapter = NetWorkUsageListAdapter(
                    context,
                    R.layout.appdata_tile,
                    outputList
                )
                noData.isVisible = false
            }
        }
    }
}