package com.corn.hyundaiproject.data.repository

import com.corn.hyundaiproject.data.car.CarPropertyDataSource
import kotlinx.coroutines.flow.StateFlow

// 실제 구현체
class CarRepositoryImpl(private val dataSource: CarPropertyDataSource) {
    // DataSource의 Flow를 그대로 ViewModel로 통과시킴
    val temperature: StateFlow<Float> = dataSource.temperature
}