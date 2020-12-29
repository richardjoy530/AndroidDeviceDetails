package com.example.androidDeviceDetails.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.androidDeviceDetails.R
import com.example.androidDeviceDetails.models.AppDataUsage
import com.example.androidDeviceDetails.utils.Utils

class AppDataListAdapter(
    private var _context: Context,
    private var resource: Int,
    private var items: ArrayList<AppDataUsage>
) : ArrayAdapter<AppDataUsage>(_context, resource, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(_context)
        var vi = convertView
        val holder: AppDataViewHolder
        if (convertView == null) {
            vi = layoutInflater.inflate(resource, null)
            holder = AppDataViewHolder()
            holder.appNameView = vi.findViewById(R.id.appName)
            holder.wifiUsageView = vi.findViewById(R.id.appWifiData)
            holder.cellularUsageView = vi.findViewById(R.id.appCellularData)
            holder.appIconView = vi.findViewById(R.id.appIcon)
            vi.tag = holder
        } else holder = vi?.tag as AppDataViewHolder

        holder.appNameView?.text = Utils.getApplicationLabel(items[position].packageName)
        var text =
            Utils.getFileSize(items[position].receivedDataWifi + items[position].transferredDataWifi)
        holder.wifiUsageView?.text = text
        text =
            Utils.getFileSize(items[position].receivedDataMobile + items[position].transferredDataMobile)
        holder.cellularUsageView?.text = text
        holder.appIconView?.setImageDrawable(Utils.getApplicationIcon(items[position].packageName))

        return vi!!
    }
}


data class AppDataViewHolder(
    var appNameView: TextView? = null,
    var wifiUsageView: TextView? = null,
    var cellularUsageView: TextView? = null,
    var appIconView: ImageView? = null,
)