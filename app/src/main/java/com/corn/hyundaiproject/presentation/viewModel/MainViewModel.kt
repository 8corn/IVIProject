package com.corn.hyundaiproject.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corn.hyundaiproject.domain.repository.CarRepository
import com.corn.hyundaiproject.domain.usecase.GetTemperatureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val getTemperatureUseCase: GetTemperatureUseCase,
    private val repository: CarRepository
) : ViewModel() {
    // Repository의 Float 데이터를 HvacState 객체로 변환해서 UI에 노출
    // collectAsState를 편하게 쓰기 위해 StateFlow를 변환하는 로직

    val uiState: StateFlow<IntegratedCarState> = combine(
        getTemperatureUseCase(),
        repository.forwardDistance,
        repository.isLaneDeparture,
        repository.drivingStatus,
        repository.vehicleDetails,
    ) { hvacInfo, distance, isDeparture, status, details ->
        // 상황 판단 로직
        val safetyLevel = when {
            isDeparture || distance < 5.0f -> SafetyLevel.DANGER
            distance < 15.0f -> SafetyLevel.CAUTION
            else -> SafetyLevel.SAFE
        }

        // 메세지 결정
        val distanceMessage = when (safetyLevel) {
            SafetyLevel.DANGER -> "즉시 제동 및 차선 유지 필요"
            SafetyLevel.CAUTION -> "전방 차량 간격 주의"
            else -> hvacInfo.warningMessage ?: "안전 운행 중"
        }

        // 통합 객체 생성
        IntegratedCarState(
            temperature = hvacInfo.temperature,
            isDoorLocked = hvacInfo.isDoorLocked,
            drivingStatus = status,
            forwardDistance = distance,
            isLaneDeparture = isDeparture,
            safetyLevel = safetyLevel,
            warningMessage = distanceMessage,
            speed = details["speed"] ?: "0"
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = IntegratedCarState()
    )

    fun updateTemperature(delta: Float) {
        val newTemp = uiState.value.temperature + delta
        repository.setTemperature(newTemp)
    }

    fun toggleDoorLock() {
        val currentLockState = uiState.value.isDoorLocked
        repository.setDoorLock(!currentLockState)
    }

    fun toggleWindow() {
        Log.d("MainViewModel", "창문 제어 명령 전송됨")
    }
}

data class IntegratedCarState(
    val temperature: Float = 22.0f,
    val isDoorLocked: Boolean = true,
    val drivingStatus: String = "상태 파악 중",
    val forwardDistance: Float = 0f,
    val isLaneDeparture: Boolean = false,
    val safetyLevel: SafetyLevel = SafetyLevel.SAFE,
    val warningMessage: String = "",
    val speed: String = "0"
)

enum class SafetyLevel { SAFE, CAUTION, DANGER }