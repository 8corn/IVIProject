package com.corn.hyundaiproject.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corn.hyundaiproject.data.repository.CarRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HvacState(
    val temperature: Float = 22.0f,
    val isAutoMode: Boolean = false,
)

class MainViewModel(private val repository: CarRepositoryImpl) : ViewModel() {
    // Repository의 Float 데이터를 HvacState 객체로 변환해서 UI에 노출
    // collectAsState를 편하게 쓰기 위해 StateFlow를 변환하는 로직
    val hvacState: StateFlow<HvacState> = repository.temperature
        .map { temp -> HvacState(temperature = temp) }  // Float -> HvacState 변환
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HvacState()
        )

    fun setTemperature(temp: Float) {
        // TODO: Repository에 setTemperature 함수 만들어서 호출해야 함
    }

    fun toggleWindow() {
        println("창문 제어 명령 전송됨")
    }
}