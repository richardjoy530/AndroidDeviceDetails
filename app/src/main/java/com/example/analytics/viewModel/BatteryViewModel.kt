package com.example.analytics.viewModel

import android.content.Context
import com.example.analytics.R
import com.example.analytics.adapters.BatteryListAdapter
import com.example.analytics.base.BaseViewModel
import com.example.analytics.cooker.BatteryCooker
import com.example.analytics.databinding.ActivityBatteryBinding
import com.example.analytics.models.battery.BatteryAppEntry
import com.example.analytics.utils.SortBy
import com.example.analytics.utils.Utils

class BatteryViewModel(private val binding: ActivityBatteryBinding, val context: Context) :
    BaseViewModel() {

    private var itemList = ArrayList<BatteryAppEntry>()
    private var sortBy = SortBy.DESCENDING.ordinal

    /**
     * This method is called once the [BatteryCooker] finishes cooking.
     * This updates the UI with the [BatteryListAdapter]
     */
    override fun <T> onDone(outputList: ArrayList<T>) {
        if (outputList.isNotEmpty()) {
            itemList = outputList.filterIsInstance<BatteryAppEntry>() as ArrayList<BatteryAppEntry>
            val totalDrop = itemList.sumOf { it.drop }
            binding.root.post {
                val totalText = "Total drop is $totalDrop %"
                binding.total.text = totalText
                sort(sortBy)
            }
        } else
            binding.root.post {
                itemList = arrayListOf()
                binding.total.text = context.getString(R.string.no_usage_recorded)
                sort(sortBy)
            }
    }

    override fun sort(type: Int) {
        sortBy = type
        when (type) {
            SortBy.DESCENDING.ordinal -> itemList.sortByDescending { it.drop }
            SortBy.ASCENDING.ordinal -> itemList.sortBy { it.drop }
            SortBy.ALPHABETICAL.ordinal -> itemList.sortBy { Utils.getApplicationLabel(it.packageId) }
            SortBy.REVERSE_ALPHABETICAL.ordinal -> itemList.sortByDescending {
                Utils.getApplicationLabel(it.packageId)
            }
        }
        val adapter = BatteryListAdapter(context, R.layout.battery_tile, itemList)
        binding.listView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}