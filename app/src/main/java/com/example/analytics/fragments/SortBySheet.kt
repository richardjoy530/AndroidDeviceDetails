package com.example.analytics.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.analytics.R
import com.example.analytics.adapters.SortOptionAdaptor
import com.example.analytics.utils.SortBy
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortBySheet(
    private val options: ArrayList<Pair<String, Int>>,
    var sortFunction: (Int) -> Unit, default: Int = SortBy.ALPHABETICAL.ordinal
) :
    BottomSheetDialogFragment() {
    private var selectedOption = default
    private var v: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.sort_by_bottomsheet, container, false)
        val listView = v?.findViewById<ListView>(R.id.listView)
        listView?.adapter =
            SortOptionAdaptor(requireContext(), R.layout.sort_by_tile, options, selectedOption)
        listView?.setOnItemClickListener { parent, _, position, _ ->
            val adaptor = parent.adapter as SortOptionAdaptor
            selectedOption = adaptor.getItem(position)?.second!!
            sortFunction(adaptor.getItem(position)?.second!!)
            this.dismiss()
        }
        return v
    }
}

