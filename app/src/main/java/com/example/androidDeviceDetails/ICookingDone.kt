package com.example.androidDeviceDetails

import java.util.*

interface ICookingDone<T> {
    fun onNoData()
    fun onData(outputList: ArrayList<T>)
}