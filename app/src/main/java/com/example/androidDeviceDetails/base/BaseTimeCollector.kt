package com.example.androidDeviceDetails.base

import java.util.*

abstract class BaseTimeCollector {
    abstract var timer: Timer
    abstract fun collect()
    abstract fun runTimer(intervalInMinuets: Long)
}


abstract class BaseCollector {
    abstract fun start()
    abstract fun collect()
    abstract fun stop()
}