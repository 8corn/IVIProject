package com.corn.hyundaiproject.data.repository

import com.corn.hyundaiproject.data.car.CarPropertyDataSource
import com.corn.hyundaiproject.domain.repository.CarRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

// 실제 구현체
@Singleton
class CarRepositoryImpl @Inject constructor(
    private val dataSource: CarPropertyDataSource
) : CarRepository {
    // DataSource의 Flow를 그대로 ViewModel로 통과시킴
    override val temperature: StateFlow<Float> = dataSource.temperature
    override val drivingStatus: StateFlow<String> = dataSource.drivingStatus
    override val climateAdvice: StateFlow<String> = dataSource.climateAdvice
    override val isDoorLocked: StateFlow<Boolean> = dataSource.isDoorLocked
    override val isWindowOpen: StateFlow<Boolean> = dataSource.isWindowOpen
    override val vehicleDetails: StateFlow<Map<String, String>> = dataSource.vehicleDetails
    override val forwardDistance: StateFlow<Float> = dataSource.forwardDistance
    override val isLaneDeparture: StateFlow<Boolean> = dataSource.isLaneDeparture
    override val fuelLevel: StateFlow<Float> = dataSource.fuelLevel

    override fun setTemperature(temp: Float) {
        dataSource.setTemperature(temp)
    }

    override fun setDoorLock(lock: Boolean) = dataSource.setDoorLock(lock)

    override fun setWindowPosition(isOpen: Boolean, areaId: Int) {
        dataSource.setWindowPosition(isOpen, areaId)
    }

    override fun closeConnection() {
        dataSource.closeConnection()
    }
}