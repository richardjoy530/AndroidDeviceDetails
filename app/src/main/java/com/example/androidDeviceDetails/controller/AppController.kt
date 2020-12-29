package com.example.androidDeviceDetails.controller

import android.content.Context
import com.example.androidDeviceDetails.ICookingDone
import com.example.androidDeviceDetails.base.BaseCooker
import com.example.androidDeviceDetails.base.BaseViewModel
import com.example.androidDeviceDetails.models.TimeInterval
import java.util.*

class AppController<T, MT>(dataType: String, binding: T, val context: Context) {

    private var cooker: BaseCooker = BaseCooker.getCooker(dataType)
    private var viewModel: BaseViewModel = BaseViewModel.getViewModel(dataType, binding, context)

    fun cook(timeInterval: TimeInterval) {
        cooker.cook(timeInterval, onCookingDone)
    }

    private val onCookingDone = object : ICookingDone<MT> {
        override fun onNoData() = viewModel.onNoData()
        override fun onData(outputList: ArrayList<MT>) =
            viewModel.onData(outputList)
    }
}

