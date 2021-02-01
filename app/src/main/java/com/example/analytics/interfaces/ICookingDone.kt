package com.example.analytics.interfaces

interface ICookingDone<T> {
    fun onDone(outputList: ArrayList<T>)
}