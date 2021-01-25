package com.example.analytics.interfaces

import com.example.analytics.models.location.LocationDisplayModel

interface OnItemClickListener {
    fun onItemClicked(clickedItem: LocationDisplayModel)
}