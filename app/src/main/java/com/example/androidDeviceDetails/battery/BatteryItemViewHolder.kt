package com.example.androidDeviceDetails.battery

import android.widget.ImageView
import android.widget.TextView

data class BatteryItemViewHolder(
    var appNameView: TextView? = null,
    var dropTextView: TextView? = null,
    var appIconView: ImageView? = null,
)