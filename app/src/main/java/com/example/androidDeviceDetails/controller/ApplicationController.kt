package com.example.androidDeviceDetails.controller

import com.example.androidDeviceDetails.DeviceDetailsApplication
import com.example.androidDeviceDetails.managers.AppEventCollector
import com.example.androidDeviceDetails.managers.NetworkUsageCollector
import com.example.androidDeviceDetails.managers.SignalChangeListener
import com.example.androidDeviceDetails.receivers.AppStateReceiver
import com.example.androidDeviceDetails.receivers.BatteryReceiver
import com.example.androidDeviceDetails.receivers.WifiReceiver

class ApplicationController {
    var mBatteryReceiver: BatteryReceiver
    var mAppStateReceiver: AppStateReceiver
    var mWifiReceiver: WifiReceiver
    var mAppEventCollector: AppEventCollector
    var mAppDataUsageCollector: NetworkUsageCollector
    var mPhoneStateListener: SignalChangeListener

    init{
        mBatteryReceiver = BatteryReceiver()
        mWifiReceiver = WifiReceiver()
        mAppStateReceiver = AppStateReceiver()
        mAppEventCollector = AppEventCollector(DeviceDetailsApplication.instance)
        mPhoneStateListener = SignalChangeListener(DeviceDetailsApplication.instance)
        mAppDataUsageCollector = NetworkUsageCollector(DeviceDetailsApplication.instance)
    }

}