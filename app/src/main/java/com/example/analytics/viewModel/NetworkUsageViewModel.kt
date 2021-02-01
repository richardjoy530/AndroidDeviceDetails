package com.example.analytics.viewModel

import android.content.Context
import androidx.core.view.isVisible
import com.example.analytics.R
import com.example.analytics.adapters.NetWorkUsageListAdapter
import com.example.analytics.base.BaseViewModel
import com.example.analytics.databinding.ActivityAppDataBinding
import com.example.analytics.models.database.AppNetworkUsageRaw
import com.example.analytics.utils.SortBy
import com.example.analytics.utils.Utils
import java.util.*

/**
 * Implements [BaseViewModel]
 */
class NetworkUsageViewModel(
    private val binding: ActivityAppDataBinding,
    val context: Context
) : BaseViewModel() {
    private var itemList = ArrayList<AppNetworkUsageRaw>()
    private var sortBy = SortBy.WIFI_DESCENDING.ordinal

    /**
     * Overrides [onDone] in [BaseViewModel]
     *
     * Display provided data on UI.
     *
     * @param outputList List of cooked data.
     */
    override fun <T> onDone(outputList: ArrayList<T>) {
        if (outputList.isNotEmpty()) {
            itemList =
                outputList.filterIsInstance<AppNetworkUsageRaw>() as ArrayList<AppNetworkUsageRaw>
            binding.root.post {
                binding.noData.isVisible = false
                sort(sortBy)
            }
        } else {
            binding.root.post {
                binding.noData.isVisible = true
                sort(sortBy)
            }
        }
    }


    override fun sort(type: Int) {
        sortBy = type
        when (type) {
            SortBy.WIFI_DESCENDING.ordinal -> itemList.sortByDescending { it.transferredDataWifi + it.receivedDataWifi }
            SortBy.WIFI_ASCENDING.ordinal -> itemList.sortBy { it.transferredDataWifi + it.receivedDataWifi }
            SortBy.CELLULAR_DESCENDING.ordinal -> itemList.sortByDescending { it.transferredDataMobile + it.receivedDataMobile }
            SortBy.CELLULAR_ASCENDING.ordinal -> itemList.sortBy { it.transferredDataMobile + it.receivedDataMobile }
            SortBy.ALPHABETICAL.ordinal -> itemList.sortBy { Utils.getApplicationLabel(it.packageName) }
            SortBy.REVERSE_ALPHABETICAL.ordinal -> itemList.sortByDescending {
                Utils.getApplicationLabel(
                    it.packageName
                )
            }
        }
        itemList =
            itemList.filterNot { it.receivedDataWifi + it.receivedDataMobile + it.transferredDataMobile + it.transferredDataWifi == 0L } as ArrayList<AppNetworkUsageRaw>
        val adapter = NetWorkUsageListAdapter(context, R.layout.appdata_tile, itemList)
        binding.appDataListView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}