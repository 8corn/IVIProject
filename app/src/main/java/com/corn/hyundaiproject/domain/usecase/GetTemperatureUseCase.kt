package com.corn.hyundaiproject.domain.usecase

import com.corn.hyundaiproject.domain.model.HvacInfo
import com.corn.hyundaiproject.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetTemperatureUseCase (
    private val repository: CarRepository
) {
    operator fun invoke(): Flow<HvacInfo> {
        return combine(
            repository.temperature,
            repository.isDoorLocked,
            repository.climateAdvice
        ) { temp, isLocked, advice ->
            val warning = if (temp >= 30f) advice else null

            HvacInfo(
                temperature = temp,
                warningMessage = warning,
                isDoorLocked =  isLocked
            )
        }
    }
}