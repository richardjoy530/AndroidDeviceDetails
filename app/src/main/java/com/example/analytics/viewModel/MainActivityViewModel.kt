package com.example.analytics.viewModel

import android.content.Context
import android.view.View.VISIBLE
import com.example.analytics.base.BaseViewModel
import com.example.analytics.databinding.ActivityMainBinding
import com.example.analytics.models.battery.BatteryAppEntry
import com.example.analytics.models.database.AppInfoRaw
import com.example.analytics.models.database.AppNetworkUsageRaw
import com.example.analytics.models.database.DeviceNetworkUsageRaw
import com.example.analytics.models.signal.SignalRaw
import com.example.analytics.utils.Utils
import kotlin.math.ceil

class MainActivityViewModel(private val binding: ActivityMainBinding, val context: Context) :
    BaseViewModel() {
    override fun <T> onDone(outputList: ArrayList<T>) {
        val appInfoList = arrayListOf<AppInfoRaw>()
        val batteryList = arrayListOf<BatteryAppEntry>()
        val dataUsageList = arrayListOf<DeviceNetworkUsageRaw>()
        val appDataUsageList = arrayListOf<AppNetworkUsageRaw>()
        val signalList = arrayListOf<SignalRaw>()
        for (i in 0 until outputList.size)
            when (outputList[i]) {
                is AppInfoRaw -> appInfoList.add(outputList[i] as AppInfoRaw)
                is BatteryAppEntry -> batteryList.add(outputList[i] as BatteryAppEntry)
                is DeviceNetworkUsageRaw -> dataUsageList.add(outputList[i] as DeviceNetworkUsageRaw)
                is AppNetworkUsageRaw -> appDataUsageList.add(outputList[i] as AppNetworkUsageRaw)
                is SignalRaw -> signalList.add(outputList[i] as SignalRaw)
            }
        binding.root.post {
            if (appInfoList.isNotEmpty()) updateAppInfoCard(appInfoList)
            if (batteryList.isNotEmpty()) updateBatteryCard(batteryList)
            if (dataUsageList.isNotEmpty()) updateDeviceNetworkUsageCard(dataUsageList)
            if (appDataUsageList.isNotEmpty()) updateAppDataUsageCard(appDataUsageList)
            if (signalList.isNotEmpty()) updateSignalDataCard(signalList)
        }
    }

    private fun updateBatteryCard(outputList: ArrayList<BatteryAppEntry>) {
        val totalDrop = outputList.sumBy { it.drop }
        outputList.sortByDescending { it.drop }
        if (outputList.size > 2) {
            binding.batteryInfo.app3.text = outputList[2].drop.toString()
            binding.batteryInfo.app3.visibility = VISIBLE
            binding.batteryInfo.app3Icon.visibility = VISIBLE
            binding.batteryInfo.app3Icon.setImageDrawable(Utils.getApplicationIcon(outputList[2].packageId))
        }
        if (outputList.size > 1) {
            binding.batteryInfo.app2.text = outputList[1].drop.toString()
            binding.batteryInfo.app2.visibility = VISIBLE
            binding.batteryInfo.app2Icon.visibility = VISIBLE
            binding.batteryInfo.app2Icon.setImageDrawable(Utils.getApplicationIcon(outputList[1].packageId))
        }
        if (outputList.size > 0) {
            binding.batteryInfo.app1.text = outputList[0].drop.toString()
            binding.batteryInfo.app1.visibility = VISIBLE
            binding.batteryInfo.app1Icon.visibility = VISIBLE
            binding.batteryInfo.app1Icon.setImageDrawable(Utils.getApplicationIcon(outputList[0].packageId))
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
        binding.networkUsage.label1Value.text =
            Utils.getFileSize(outputList.first().transferredDataWifi + outputList.first().receivedDataWifi)
        binding.networkUsage.label2Value.text =
            Utils.getFileSize(outputList.first().transferredDataMobile + outputList.first().transferredDataMobile)
    }

    private fun updateAppDataUsageCard(outputList: ArrayList<AppNetworkUsageRaw>) {
        outputList.sortByDescending { getFullUsageData(it) }
        if (outputList.size > 2) {
            binding.networkUsage.app3.text = Utils.getFileSize(getFullUsageData(outputList[2]))
            binding.networkUsage.app3.visibility = VISIBLE
            binding.networkUsage.app3Icon.visibility = VISIBLE
            binding.networkUsage.app3Icon.setImageDrawable(Utils.getApplicationIcon(outputList[2].packageName))
        }
        if (outputList.size > 1) {
            binding.networkUsage.app2.text = Utils.getFileSize(getFullUsageData(outputList[1]))
            binding.networkUsage.app2.visibility = VISIBLE
            binding.networkUsage.app2Icon.visibility = VISIBLE
            binding.networkUsage.app2Icon.setImageDrawable(Utils.getApplicationIcon(outputList[1].packageName))
        }
        if (outputList.size > 0) {
            binding.networkUsage.app1.text = Utils.getFileSize(getFullUsageData(outputList[0]))
            binding.networkUsage.app1.visibility = VISIBLE
            binding.networkUsage.app1Icon.visibility = VISIBLE
            binding.networkUsage.app1Icon.setImageDrawable(Utils.getApplicationIcon(outputList[0].packageName))
        }
    }


    private fun updateSignalDataCard(outputList: ArrayList<SignalRaw>) {
        binding.signalData.pointerCellularSpeedometer.speedTo(50F, 1000)
        binding.signalData.pointerWifiSpeedometer.speedTo(70F, 1000)
    }

    private fun graphCalculator(dataSize: Int, total: Int) =
        ceil(((dataSize).toDouble().div(total).times(100))).toInt()

    private fun getFullUsageData(app: AppNetworkUsageRaw) =
        app.receivedDataMobile + app.receivedDataWifi + app.transferredDataMobile + app.transferredDataWifi


}