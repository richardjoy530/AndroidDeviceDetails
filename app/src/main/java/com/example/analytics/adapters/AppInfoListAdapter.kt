package com.example.analytics.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.analytics.R
import com.example.analytics.models.appInfo.AppInfoCookedData
import com.example.analytics.models.appInfo.AppInfoItemViewHolder
import com.example.analytics.models.appInfo.EventType
import com.example.analytics.models.appInfo.ProgressbarViewHolder
import com.example.analytics.utils.Utils
import kotlin.math.ceil


class AppInfoListAdapter(
    private var _context: Context,
    private var resource: Int,
    private var items: List<AppInfoCookedData>,
    private var appList: List<AppInfoCookedData>
) : ArrayAdapter<AppInfoCookedData>(_context, resource, items) {

    override fun getViewTypeCount(): Int {
        return 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            0
        } else {
            1
        }
    }

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(_context)
        var vi = convertView
        if (position == 0) {
            var holder: ProgressbarViewHolder?
            if (convertView == null) {
                vi = layoutInflater.inflate(R.layout.appinfo_pie_chart, null)
                holder = ProgressbarViewHolder(
                    vi.findViewById(R.id.updated_progressBar),
                    vi.findViewById(R.id.enroll_progressbar),
                    vi.findViewById(R.id.installed_progressBar),
                    vi.findViewById(R.id.uninstalled_progressbar),
                    vi.findViewById(R.id.enroll_count),
                    vi.findViewById(R.id.install_count),
                    vi.findViewById(R.id.update_count),
                    vi.findViewById(R.id.uninstall_count)
                )
                vi.tag = holder
            } else holder = vi?.tag as ProgressbarViewHolder
            holder = setProgressbarHolder(holder)

        } else {
            var holder: AppInfoItemViewHolder?
            if (convertView == null) {
                vi = layoutInflater.inflate(resource, null)
                holder = AppInfoItemViewHolder(
                    vi.findViewById(R.id.appName),
                    vi.findViewById(R.id.appVersionCode),
                    vi.findViewById(R.id.appEvent),
                    vi.findViewById(R.id.appIcon),
                    vi.findViewById(R.id.event_badge),
                    vi.findViewById(R.id.uninstall_button)
                )
                vi.tag = holder
            } else holder = vi?.tag as AppInfoItemViewHolder
            holder = setAppInfoHolder(holder, position)
        }
        return vi!!
    }

    private fun setProgressbarHolder(holder: ProgressbarViewHolder): ProgressbarViewHolder {
        val total = appList.size.toDouble()
        val enrolledAppCount =
            appList.groupingBy { it.eventType.ordinal == EventType.APP_ENROLL.ordinal }
                .eachCount()
        val enrolled = ((enrolledAppCount[true] ?: 0).toDouble().div(total).times(100))

        val installedAppCount =
            appList.groupingBy { it.eventType.ordinal == EventType.APP_INSTALLED.ordinal }
                .eachCount()
        val installed =
            ceil(((installedAppCount[true] ?: 0).toDouble().div(total).times(100)))

        val updateAppCount =
            appList.groupingBy { it.eventType.ordinal == EventType.APP_UPDATED.ordinal }
                .eachCount()
        val updated = ceil(((updateAppCount[true] ?: 0).toDouble().div(total).times(100)))

        val uninstalledAppCount =
            appList.groupingBy { it.eventType.ordinal == EventType.APP_UNINSTALLED.ordinal }
                .eachCount()
        val uninstalled =
            ceil(((uninstalledAppCount[true] ?: 0).toDouble().div(total).times(100)))

        holder.updated_progressBar.progress = (updated.toInt())
        holder.installed_progressBar.progress = (updated + installed).toInt()
        holder.enroll_progressbar.progress = (updated + installed + enrolled).toInt()
        holder.uninstalled_progressbar.progress =
            (updated + installed + enrolled + uninstalled).toInt()
        holder.enroll_count.text = (enrolledAppCount[true] ?: 0).toString()
        holder.install_count.text = (installedAppCount[true] ?: 0).toString()
        holder.update_count.text = (updateAppCount[true] ?: 0).toString()
        holder.uninstall_count.text = (uninstalledAppCount[true] ?: 0).toString()
        return holder
    }

    @SuppressLint("SetTextI18n")
    private fun setAppInfoHolder(
        holder: AppInfoItemViewHolder,
        position: Int
    ): AppInfoItemViewHolder {
        holder.appNameView.text = items[position].appName
        holder.versionCodeTextView.text =
            "Version Code : " + items[position].versionCode.toString()
        holder.eventTypeTextView.text = " | Event : " + items[position].eventType.toString()
        holder.appIconView.setImageDrawable(Utils.getApplicationIcon(items[position].packageName))
        val color = when (items[position].eventType.ordinal) {
            0 -> R.color.teal_700
            1 -> R.color.purple_500
            2 -> R.color.pink
            3 -> R.color.mat_yellow
            else -> R.color.teal_700
        }
        holder.uninstallButton.isVisible = true
        holder.eventBadge.setColorFilter(
            ContextCompat.getColor(context, color),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )
        holder.uninstallButton.tag = items[position].packageName
        if (items[position].eventType.ordinal == EventType.APP_ENROLL.ordinal) {
            holder.eventBadge.isVisible = false
        }
        if (items[position].isSystemApp) {
            holder.uninstallButton.isVisible = false
        } else if (items[position].eventType == EventType.APP_UNINSTALLED) {
            holder.uninstallButton.isVisible = false
        }
        return holder
    }

}