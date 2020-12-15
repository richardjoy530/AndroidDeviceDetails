package com.example.androidDeviceDetails.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.androidDeviceDetails.R
import com.example.androidDeviceDetails.managers.AppEntry
import com.example.androidDeviceDetails.utils.Utils


class BatteryListAdapter(
    private var _context: Context,
    private var resource: Int,
    private var items: ArrayList<AppEntry>
) : ArrayAdapter<AppEntry>(_context, resource, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(_context)
        var vi = convertView
        val holder: ViewHolder
        if (convertView == null) {
            vi = layoutInflater.inflate(resource, null)
            holder = ViewHolder()
            holder.appNameView = vi.findViewById(R.id.appName)
            holder.dropTextView = vi.findViewById(R.id.dropText)
            holder.appIconView = vi.findViewById(R.id.appIcon)
            vi.tag = holder
        } else holder = vi?.tag as ViewHolder

        holder.appNameView?.text = Utils.getApplicationLabel(items[position].packageId)
        val text = "Dropped " + items[position].drop.toString() + " %"
        holder.dropTextView?.text = text
        holder.appIconView?.setImageDrawable(Utils.getApplicationIcon(items[position].packageId))

        return vi!!
    }
}

data class ViewHolder(
    var appNameView: TextView? = null,
    var dropTextView: TextView? = null,
    var appIconView: ImageView? = null,
)