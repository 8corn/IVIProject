package com.corn.hyundaiproject.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface CarRepository {
    val temperature: StateFlow<Float>
    val drivingStatus: StateFlow<String>
    val climateAdvice: StateFlow<String>
    val isDoorLocked: StateFlow<Boolean>
    val isWindowOpen: StateFlow<Boolean>
    val vehicleDetails: StateFlow<Map<String, String>>
    suspend fun fetchLatestVehicleDetails()
    val forwardDistance: StateFlow<Float>
    val isLaneDeparture: StateFlow<Boolean>
    val fuelLevel: StateFlow<Float>

    fun setTemperature(temp: Float)
    fun setDoorLock(lock: Boolean)
    fun setWindowPosition(isOpen: Boolean, areaId: Int)
    fun closeConnection()
}