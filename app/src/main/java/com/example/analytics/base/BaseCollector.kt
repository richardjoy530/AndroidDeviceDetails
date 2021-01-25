package com.example.analytics.base

abstract class BaseCollector {
    open fun start() {}
    open fun collect() {}
    open fun stop() {}
}