package com.corn.hyundaiproject.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corn.hyundaiproject.data.repository.CarRepositoryImpl
import com.corn.hyundaiproject.domain.usecase.GetTemperatureUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    private val getTemperatureUseCase: GetTemperatureUseCase,
    private val repository: CarRepositoryImpl
) : ViewModel() {
    // Repository의 Float 데이터를 HvacState 객체로 변환해서 UI에 노출
    // collectAsState를 편하게 쓰기 위해 StateFlow를 변환하는 로직

    // 현재 상태 요약본 -> 신호를 받아야 화면을 다시 그리고 hvacState는 StateFlow라는 파이프라인을 통해 그 신호를 보내주는 역할
    val hvacState: StateFlow<HvacState> = getTemperatureUseCase()   // usecase가 차로부터 온도 받아와서 위험한지 판단
        .map { info ->
            HvacState(
                temperature = info.temperature,
                warningMessage = info.warningMessage,
                isDoorLocked = info.isDoorLocked
            )
        }       // map 함수가 판단 결과(HvacInfo)를 UI가 이해하는 쉬운 HvacState로 변환
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HvacState()
        )       // stateIn은 이 파이프라인을 뷰모델이 살아있는 동안 계속 유지

    val drivingStatus: StateFlow<String> = repository.drivingStatus
    val climateAdvice: StateFlow<String> = repository.climateAdvice

    fun updateTemperature(delta: Float) {
        val currentTemp = hvacState.value.temperature
        val newTemp = currentTemp + delta
        repository.setTemperature(newTemp)
    }

    fun toggleDoorLock() {
        val currentLockState = hvacState.value.isDoorLocked
        repository.setDoorLock(!currentLockState)
    }

    fun toggleWindow() {
        println("창문 제어 명령 전송됨")
    }
}

data class HvacState(
    val temperature: Float = 22.0f,
    val isAutoMode: Boolean = false,
    val isDoorLocked: Boolean = true,
    val warningMessage: String? =null
)