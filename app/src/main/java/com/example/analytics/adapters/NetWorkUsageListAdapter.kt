package com.example.analytics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.analytics.R
import com.example.analytics.models.database.AppNetworkUsageRaw
import com.example.analytics.models.networkUsage.NetworkUsageItemViewHolder
import com.example.analytics.utils.Utils

class NetWorkUsageListAdapter(
    private var _context: Context, private var resource: Int,
    private var items: ArrayList<AppNetworkUsageRaw>
) : ArrayAdapter<AppNetworkUsageRaw>(_context, resource, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(_context)
        var vi = convertView
        val holder: NetworkUsageItemViewHolder
        if (convertView == null) {
            vi = layoutInflater.inflate(resource, null)
            holder = NetworkUsageItemViewHolder()
            holder.appNameView = vi.findViewById(R.id.appName)
            holder.wifiUsageView = vi.findViewById(R.id.appWifiData)
            holder.cellularUsageView = vi.findViewById(R.id.appCellularData)
            holder.appIconView = vi.findViewById(R.id.appIcon)
            vi.tag = holder
        } else holder = vi?.tag as NetworkUsageItemViewHolder

        holder.appNameView?.text = Utils.getApplicationLabel(items[position].packageName)
        var text =
            Utils.getFileSize(items[position].receivedDataWifi + items[position].transferredDataWifi)
        holder.wifiUsageView?.text = text
        text =
            Utils.getFileSize(items[position].receivedDataMobile + items[position].transferredDataMobile)
        holder.cellularUsageView?.text = text
        if (items[position].receivedDataMobile + items[position].transferredDataMobile == 0L)
            holder.cellularUsageView?.visibility = GONE
        if (items[position].receivedDataWifi + items[position].receivedDataWifi == 0L)
            holder.wifiUsageView?.visibility = GONE
        holder.appIconView?.setImageDrawable(Utils.getApplicationIcon(items[position].packageName))

        return vi!!
    }
}


