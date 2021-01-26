package com.example.analytics.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.analytics.R
import com.example.analytics.controller.ActivityController
import com.example.analytics.databinding.ActivityMainBinding
import com.example.analytics.services.AppService
import com.example.analytics.utils.PrefManager
import com.example.analytics.utils.Utils

const val permissionCode = 200
val permissions: Array<String> =
    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE)

class MainActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val NAME = "Main Activity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityController: ActivityController<Any>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityController = ActivityController(
            NAME, binding, this,
            null, supportFragmentManager = supportFragmentManager
        )
        supportActionBar?.hide()
        requestPermissions()
        if (!PrefManager.createInstance(this).getBoolean(PrefManager.INITIAL_LAUNCH, false)) {
            Utils.addInitialData(this)
            PrefManager.createInstance(this).putBoolean(PrefManager.INITIAL_LAUNCH, true)
        }
        binding.apply {
            binding.appInfo.cardView.setOnClickListener(this@MainActivity)
            binding.batteryInfo.cardView.setOnClickListener(this@MainActivity)
            binding.networkUsage.cardView.setOnClickListener(this@MainActivity)
            binding.signalData.cardView.setOnClickListener(this@MainActivity)
        }

        binding.pullToRefresh.setProgressBackgroundColorSchemeResource(R.color.app_green)
        binding.pullToRefresh.setOnRefreshListener {
            mainActivityController.refreshCooker()
            binding.pullToRefresh.isRefreshing = false
        }
    }

    private fun requestPermissions() =
        ActivityCompat.requestPermissions(this, permissions, permissionCode)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_DENIED || grantResults[1] == PackageManager.PERMISSION_DENIED)
            Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show()
        else {
            if (!Utils.isUsageAccessGranted(this))
                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                startForegroundService(Intent(this, AppService::class.java))
            else startService(Intent(this, AppService::class.java))
        }
    }

    override fun onClick(v: View?) {
        when (v!!) {
            binding.batteryInfo.cardView ->
                startActivity(Intent(this, BatteryActivity::class.java))
            binding.appInfo.cardView ->
                startActivity(Intent(this, AppInfoActivity::class.java))
            binding.networkUsage.cardView ->
                startActivity(Intent(this, NetworkUsageActivity::class.java))
        }
    }
}