package com.example.analytics.viewModel

import android.content.Context
import com.example.analytics.DeviceDetailsApplication
import com.example.analytics.R
import com.example.analytics.adapters.AppInfoListAdapter
import com.example.analytics.base.BaseViewModel
import com.example.analytics.databinding.ActivityAppInfoBinding
import com.example.analytics.models.appInfo.AppInfoCookedData
import com.example.analytics.models.appInfo.EventType

/**
 * Implements [BaseViewModel]
 */
class AppInfoViewModel(private val binding: ActivityAppInfoBinding, val context: Context) :
    BaseViewModel() {
    companion object {
        var eventFilter = 4
        var savedAppList = arrayListOf<AppInfoCookedData>()
    }

    /**
     * Displays provided data on UI as List view and a donut chart
     *
     * Overrides : [onDone] in [BaseViewModel]
     * @param [outputList] list of cooked data
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T> onDone(outputList: ArrayList<T>) {
        val appList = outputList as ArrayList<AppInfoCookedData>
        var filteredList = appList.toMutableList()
        savedAppList = appList

        if (eventFilter != EventType.ALL_EVENTS.ordinal) {
            filteredList.removeAll { it.eventType.ordinal != eventFilter }
        }
        filteredList = filteredList.sortedBy { it.appName }.toMutableList()
        if (appList.isNotEmpty()) filteredList.add(0, appList[0])
        filteredList.removeAll { it.packageName == DeviceDetailsApplication.instance.packageName }
        binding.root.post {
            binding.appInfoListView.adapter =
                AppInfoListAdapter(context, R.layout.appinfo_tile, filteredList, appList)
        }
    }

    /**
     * Filters [savedAppList] based on given filter type
     *
     * Overrides : [onDone] in [BaseViewModel]
     * @param [type] Type of filter
     */
    override fun filter(type: Int) {
        eventFilter = type
        onDone(savedAppList)
    }
}