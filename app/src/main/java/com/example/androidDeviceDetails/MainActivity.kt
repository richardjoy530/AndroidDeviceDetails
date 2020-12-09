package com.example.androidDeviceDetails

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

const val permissionCode = 200
val permissions: Array<String> =
    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE)
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var toLocationActivityButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toLocationActivityButton = findViewById(R.id.toLocationActivity)
        toLocationActivityButton.setOnClickListener(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(Intent(this, ForegroundService::class.java))
            this.startForegroundService(Intent(this, SignalService::class.java))
        } else {
            this.startService(Intent(this, ForegroundService::class.java))
            this.startService(Intent(this, SignalService::class.java))
        }
        val button = findViewById<Button>(R.id.appInfo)
        button?.setOnClickListener()
        {
            Toast.makeText(this, "Helloo", Toast.LENGTH_SHORT).show()
        }
        val toggleService = findViewById<Button>(R.id.toggleSwitch)
        toggleService.setOnClickListener {
            val mainController=MainController()
            mainController.toggleService(this)
            mainController.getAppBatteryUsage(System.currentTimeMillis()-48*60*60*1000,System.currentTimeMillis())
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.toLocationActivity -> {
                val intent = Intent(this, LocationActivity::class.java).apply {}
                startActivity(intent)
            }
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            permissions,
            permissionCode
        )
    }
}