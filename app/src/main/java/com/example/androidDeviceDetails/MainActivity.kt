package com.example.androidDeviceDetails

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.androidDeviceDetails.utils.EventType
import com.example.androidDeviceDetails.models.RoomDB
import com.example.androidDeviceDetails.services.CollectorService
import com.example.androidDeviceDetails.utils.PrefManager
import com.example.androidDeviceDetails.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val permissionCode = 200
val permissions: Array<String> =
    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE)
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var toLocationActivityButton: Button
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()
        if(PrefManager.initialLaunch(this))
            Utils.addInitialData(this)
        toLocationActivityButton = findViewById(R.id.toLocationActivity)
        toLocationActivityButton.setOnClickListener(this)
        val button = findViewById<Button>(R.id.appInfo)
        var text = findViewById<TextView>(R.id.textView)
        button?.setOnClickListener()
        {
            val db = RoomDB.getDatabase()!!
            Toast.makeText(this, "Helloo", Toast.LENGTH_SHORT).show()
            GlobalScope.launch(Dispatchers.IO) {
                var apps = db.appsDao().getAll()
                for(app in apps){
                    val initalAppState = db.appHistoryDao().getInitialData(app.uid)
                    val latestAppState = db.appHistoryDao().getLastRecord(app.uid)
                    if(latestAppState.versionCode != null) {
                        if (initalAppState.versionCode!! < latestAppState.versionCode!!) {
                            text.text = latestAppState.appTitle + "\n"
                        } else if (initalAppState.appTitle!! < latestAppState.appTitle!!) {
                            text.text = latestAppState.appTitle + "\n"
                        } else if (latestAppState.eventType == EventType.APP_UNINSTALLED.ordinal) {
                            text.text = latestAppState.appTitle + "\n"
                        }
                    }

                }
            }
        }
        val toggleService = findViewById<Button>(R.id.toggleSwitch)
        toggleService.setOnClickListener {
            val mainController=MainController()
            mainController.toggleService(this)
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