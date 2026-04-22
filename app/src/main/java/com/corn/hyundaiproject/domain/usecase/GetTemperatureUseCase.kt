package com.corn.hyundaiproject.domain.usecase

import com.corn.hyundaiproject.data.repository.CarRepositoryImpl
import com.corn.hyundaiproject.domain.model.HvacInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTemperatureUseCase (
    private val repository: CarRepositoryImpl
) {
    operator fun invoke(): Flow<HvacInfo> {
        return combine(
            repository.temperature,
            repository.isDoorLocked
        ) { temp, isLocked ->
            val warning = when {
                temp >= 30f -> "실내 온도가 너무 높습니다! 에어컨을 가동하세요."
                temp <= 5f -> "실내 온도가 너무 낮습니다! 히터를 가동하세요."
                else -> null
            }

            HvacInfo(
                temperature = temp,
                warningMessage = warning,
                isDoorLocked =  isLocked
            )
        }
    }
}