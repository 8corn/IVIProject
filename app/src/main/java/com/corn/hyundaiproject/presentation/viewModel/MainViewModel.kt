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
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.corn.hyundaiproject.domain.model.HvacInfo

@HiltViewModel
class MainViewModel @Inject constructor (
    private val getTemperatureUseCase: GetTemperatureUseCase,
    private val repository: CarRepository
) : ViewModel() {
    // Repository의 Float 데이터를 HvacState 객체로 변환해서 UI에 노출
    // collectAsState를 편하게 쓰기 위해 StateFlow를 변환하는 로직

    private var isNavigatingTrigger = false
    private var showDialogTrigger = true
    private var hasDismissedTrigger = false

    private var lastUpdateTime: Long = System.currentTimeMillis()
    private var remainDistanceMeter: Float = 1200f

    private val adasFlow = combine(
        repository.forwardDistance,
        repository.isLaneDeparture
    ) { distance, isDeparture ->
        Pair(distance, isDeparture)
    }

    @Suppress("UNCHECKED_CAST")
    val uiState: StateFlow<IntegratedCarState> = combine(
        getTemperatureUseCase(),
        adasFlow,
        repository.drivingStatus,
        repository.vehicleDetails,
        repository.fuelLevel,
        repository.isWindowOpen,
    ) { flows ->
        val hvacInfo = flows[0] as HvacInfo
        val adas = flows[1] as Pair<Float, Boolean>
        val status = flows[2] as String
        val details = flows[3] as Map<String, String>
        val fuel = flows[4] as Float
        val windowOpen = flows[5] as Boolean

        Log.d("MainViewModel", "수신된 맵 데이터: $details")

        val (distance, isDeparture) = adas
        val speedFloat = details["speed"]?.toFloatOrNull() ?: 0f

        val currentTime = System.currentTimeMillis()
        val deltaTimeSec = (currentTime - lastUpdateTime) / 1000f
        lastUpdateTime = currentTime

        if (isNavigatingTrigger && remainDistanceMeter > 0f) {
            val speedMeterPerSec = speedFloat / 3.6f
            remainDistanceMeter -= speedMeterPerSec * deltaTimeSec
            if (remainDistanceMeter < 0f) remainDistanceMeter = 0f
        }

        val roadOffset = if (isNavigatingTrigger) {
            (currentTime * (speedFloat / 10f) % 1000) / 10f
        } else {
            0f
        }

        // 상황 판단 로직
        val safetyLevel = when {
            isDeparture || distance < 5.0f -> SafetyLevel.DANGER
            distance < 15.0f -> SafetyLevel.CAUTION
            else -> SafetyLevel.SAFE
        }

        // 메세지 결정
        val distanceMessage = when {
            safetyLevel == SafetyLevel.DANGER -> "즉시 제동 및 차선 유지 필요"
            safetyLevel == SafetyLevel.CAUTION -> "전방 차량 간격 주의"
            fuel < 15f && !isNavigatingTrigger -> "연료 부족! 근처 주유소 검색이 필요합니다."
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
            speed = details["speed"] ?: "0",
            rpm = details["rpm"]?.toFloatOrNull() ?: 0f,
            engineTemp = details["engine_temp"] ?: "90.0",
            isWindowOpen = windowOpen,
            fuelLevel =fuel,
            showFuelDialog = fuel < 15f && showDialogTrigger && !hasDismissedTrigger,
            hasDismissedFuelDialog = hasDismissedTrigger,
            isNavigatingToGasStation = isNavigatingTrigger,
            remainDistance = remainDistanceMeter,
            roadOffset = roadOffset,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = IntegratedCarState()
    )

    // 팝업 주요소 검색을 눌렀을 때
    fun startGasStationNavigation() {
        Log.d("MainViewModel", "시나리오: 근처 최저가 주유소로 경로를 안내합니다.")

        isNavigatingTrigger = true
        showDialogTrigger = false
    }

    // 팝업 닫기 눌렀을 때
    fun dismissFuelDialog() {
        showDialogTrigger = false
        hasDismissedTrigger = true
    }

    fun updateTemperature(delta: Float) {
        val newTemp = uiState.value.temperature + delta
        repository.setTemperature(newTemp)
    }

    fun toggleDoorLock() {
        val currentLockState = uiState.value.isDoorLocked
        repository.setDoorLock(!currentLockState)
    }

    fun toggleWindow() {
        val nextWindowState = !uiState.value.isWindowOpen
        Log.d("MainViewModel", "창문 제어 명령 전송됨 - 상태: $nextWindowState")

        repository.setWindowPosition(isOpen = nextWindowState, areaId = 1)
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
    val speed: String = "0",
    val rpm: Float = 0f,
    val engineTemp: String = "90.0",
    val isWindowOpen: Boolean = false,
    val fuelLevel: Float = 100f,
    val showFuelDialog: Boolean = false,
    val hasDismissedFuelDialog: Boolean = false,
    val isNavigatingToGasStation: Boolean = false,
    val remainDistance: Float = 1200f,
    val roadOffset: Float = 0f
)

enum class SafetyLevel { SAFE, CAUTION, DANGER }