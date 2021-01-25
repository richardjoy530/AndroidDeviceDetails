package com.example.analytics.viewModel

import android.content.Context
import android.view.View.VISIBLE
import com.example.analytics.base.BaseViewModel
import com.example.analytics.databinding.ActivityMainBinding
import com.example.analytics.models.battery.BatteryAppEntry
import com.example.analytics.models.database.AppInfoRaw
import com.example.analytics.models.database.DeviceNetworkUsageRaw
import com.example.analytics.models.signal.SignalRaw
import com.example.analytics.utils.Utils
import kotlin.math.ceil
import kotlin.math.pow


class MainActivityViewModel(private val binding: ActivityMainBinding, val context: Context) :
    BaseViewModel() {
    override fun <T> onDone(outputList: ArrayList<T>) {
        val appInfoList = arrayListOf<AppInfoRaw>()
        val batteryList = arrayListOf<BatteryAppEntry>()
        val dataUsageList = arrayListOf<DeviceNetworkUsageRaw>()
        val signalList = arrayListOf<SignalRaw>()
        try {
            for (i in 0 until outputList.size) {
                when (outputList[i]) {
                    is AppInfoRaw -> appInfoList.add(outputList[i] as AppInfoRaw)
                    is BatteryAppEntry -> batteryList.add(outputList[i] as BatteryAppEntry)
                    is DeviceNetworkUsageRaw -> dataUsageList.add(outputList[i] as DeviceNetworkUsageRaw)
                    is SignalRaw -> signalList.add(outputList[i] as SignalRaw)
                }
            }
        } catch (e: Exception) {
        }

        binding.root.post {
            if (appInfoList.isNotEmpty()) updateAppInfoCard(appInfoList)
            if (batteryList.isNotEmpty()) updateBatteryCard(batteryList)
            if (dataUsageList.isNotEmpty()) updateDeviceNetworkUsageCard(dataUsageList)
            if (signalList.isNotEmpty()) updateSignalDataCard(signalList)
        }
    }

    private fun updateBatteryCard(outputList: ArrayList<BatteryAppEntry>) {
        val totalDrop = outputList.sumBy { it.drop }
        outputList.sortByDescending { it.drop }
        if (outputList.size > 2) {
            (outputList[2].drop.toString() + "%").also { binding.batteryInfo.app1.text = it }
            binding.batteryInfo.app1.visibility = VISIBLE
            binding.batteryInfo.app1Icon.visibility = VISIBLE
            binding.batteryInfo.app1Icon.setImageDrawable(Utils.getApplicationIcon(outputList[2].packageId))
        }
        if (outputList.size > 1) {
            (outputList[1].drop.toString() + "%").also { binding.batteryInfo.app2.text = it }
            binding.batteryInfo.app2.visibility = VISIBLE
            binding.batteryInfo.app2Icon.visibility = VISIBLE
            binding.batteryInfo.app2Icon.setImageDrawable(Utils.getApplicationIcon(outputList[1].packageId))
        }
        if (outputList.size > 0) {
            (outputList[0].drop.toString() + "%").also { binding.batteryInfo.app3.text = it }
            binding.batteryInfo.app3.visibility = VISIBLE
            binding.batteryInfo.app3Icon.visibility = VISIBLE
            binding.batteryInfo.app3Icon.setImageDrawable(Utils.getApplicationIcon(outputList[0].packageId))
        }
        binding.batteryInfo.mainValue.text = totalDrop.toString()
    }

    private fun updateAppInfoCard(outputList: ArrayList<AppInfoRaw>) {
        val systemAppsCount = outputList.filter { it.isSystemApp }.size
        val userAppsCount = outputList.size - systemAppsCount
        val systemAppsProgress = graphCalculator(systemAppsCount, outputList.size)
        val userAppsProgress = graphCalculator(userAppsCount, outputList.size)
        binding.appInfo.label1Value.text = systemAppsCount.toString()
        binding.appInfo.label2Value.text = userAppsCount.toString()
        binding.appInfo.progressbarFirst.progress = systemAppsProgress + userAppsProgress
        binding.appInfo.progressbarSecond.progress = userAppsProgress
    }

    private fun updateDeviceNetworkUsageCard(outputList: ArrayList<DeviceNetworkUsageRaw>) {
        val wifiData =
            (outputList.first().transferredDataWifi + outputList.first().receivedDataWifi) /
                    1024.0.pow(2.toDouble())
        val cellularData =
            (outputList.first().transferredDataMobile + outputList.first().transferredDataMobile) /
                    1024.0.pow(2.toDouble())
        val total = wifiData + cellularData
        val wifiDataProgress = graphCalculator(wifiData.toInt(), total.toInt())
        val cellularDataProgress = graphCalculator(cellularData.toInt(), total.toInt())
        binding.networkUsage.label1Value.text =
            Utils.getFileSize(outputList.first().transferredDataWifi + outputList.first().receivedDataWifi)
        binding.networkUsage.label2Value.text =
            Utils.getFileSize(outputList.first().transferredDataMobile + outputList.first().transferredDataMobile)
        binding.networkUsage.progressbarFirst.progress = wifiDataProgress + cellularDataProgress
        binding.networkUsage.progressbarSecond.progress = cellularDataProgress
    }

    private fun updateSignalDataCard(outputList: ArrayList<SignalRaw>) {
        binding.signalData.pointerCellularSpeedometer.speedTo(50F, 1000)
        binding.signalData.pointerWifiSpeedometer.speedTo(70F, 1000)
    }

    private fun graphCalculator(dataSize: Int, total: Int) =
        ceil(((dataSize).toDouble().div(total).times(100))).toInt()
}