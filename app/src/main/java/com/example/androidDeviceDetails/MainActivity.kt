package com.example.androidDeviceDetails

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var toLocationActivityButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toLocationActivityButton = findViewById(R.id.toLocationActivity)
        toLocationActivityButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.toLocationActivity -> {
                val intent = Intent(this, LocationActivity::class.java).apply {}
                startActivity(intent)
            }
        }
    }
}