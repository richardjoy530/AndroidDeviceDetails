package com.example.analytics.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.analytics.R
import com.example.analytics.controller.ActivityController
import com.example.analytics.databinding.ActivitySignalBinding
import com.example.analytics.models.signal.SignalRaw
import com.example.analytics.utils.Signal

class SignalActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySignalBinding
    private lateinit var signalController: ActivityController<SignalRaw>

    companion object {
        const val NAME = "signal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signal)
        binding.lifecycleOwner = this
        signalController = ActivityController(
            NAME, binding, this, binding.pickerBinding, supportFragmentManager
        )
        binding.apply {
            pickerBinding.startTime.setOnClickListener(this@SignalActivity)
            pickerBinding.startDate.setOnClickListener(this@SignalActivity)
            pickerBinding.endTime.setOnClickListener(this@SignalActivity)
            pickerBinding.endDate.setOnClickListener(this@SignalActivity)
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.cellular -> {
                    signalController.filterView(Signal.CELLULAR.ordinal)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.wifi -> {
                    signalController.filterView(Signal.WIFI.ordinal)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.startTime -> signalController.setTime(this, R.id.startTime)
            R.id.startDate -> signalController.setDate(this, R.id.startDate)
            R.id.endTime -> signalController.setTime(this, R.id.endTime)
            R.id.endDate -> signalController.setDate(this, R.id.endDate)
        }
    }

}