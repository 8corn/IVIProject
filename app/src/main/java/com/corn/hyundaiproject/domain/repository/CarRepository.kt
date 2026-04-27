package com.corn.hyundaiproject.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface CarRepository {
    val temperature: StateFlow<Float>
    val drivingStatus: StateFlow<String>
    val climateAdvice: StateFlow<String>
    val isDoorLocked: StateFlow<Boolean>
    val vehicleDetails: StateFlow<Map<String, String>>

    fun setTemperature(temp: Float)
    fun setDoorLock(lock: Boolean)
    fun closeConnection()
}