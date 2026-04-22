package com.corn.hyundaiproject.data.car

import android.car.VehiclePropertyIds
import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CarPropertyDataSource(context: Context) {

    // C++ 라이브러리를 메모리에 로드
    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    // C++에 있는 함수를 정의 -> 함수 이름이 c++ 파일의 이름과 동일해야 함
    private external fun getEfficiencyGrade(fuelEfficiency: Float): Int
    private external fun checkDrivingStatus(speed: Float): String
    private external fun changeHeadlight(night: Boolean): Int
    private external fun getClimateAdvice(exteriorTemp: Float, interiorTemp: Float): String
    private external fun checkFuelStatus(fuelLevel: Float): String
    private external fun isHazardous(gear: Int, isDoorOpen: Boolean): Boolean
    private external fun getDetailedCarData(speed: Float): Map<String, String>

    // 상태 저장용 변수 (상태 판단을 위해 필요)
    private var currentGear: Int = 0
    private var isAnyDoorOpen: Boolean = false
    private var currentExteriorTemp: Float = 20f

    // 실시간 온도를 담는 StateFlow (Helper에서 온 데이터이자 UI에 뿌려줄 데이터 업데이트)
    private val _drivingStatus = MutableStateFlow("상태 파악 중...")
    val drivingStatus: StateFlow<String> = _drivingStatus.asStateFlow()

    private val _temperature = MutableStateFlow(22f)
    val temperature: StateFlow<Float> = _temperature.asStateFlow()

    private val _climateAdvice = MutableStateFlow("쾌적합니다.")
    val climateAdvice: StateFlow<String> = _climateAdvice.asStateFlow()

    private val _isDoorLocked = MutableStateFlow(true)
    val isDoorLocked: StateFlow<Boolean> = _isDoorLocked.asStateFlow()

    private val _vehicleDetails = MutableStateFlow(
        mapOf("rpm" to "0", "drive_mode" to "NORMAL", "engine_temp" to "90.5")
    )
    val vehicleDetails: StateFlow<Map<String, String>> = _vehicleDetails.asStateFlow()

    // Helper를 생성하면서 콜백(람다)을 전달
    // Helper에서 onTemperatureChanged(temp)를 호출하면 이 블록이 실행됨
    private val helper = CarPropertyManagerHelper(context) { propertyId, value ->
        when (propertyId) {
            VehiclePropertyIds.HVAC_TEMPERATURE_SET -> {
                val temp = value as Float
                _temperature.value = temp
                _climateAdvice.value = getClimateAdvice(currentExteriorTemp, temp)
            }
            VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE -> {
                currentExteriorTemp = value as Float
            }
            VehiclePropertyIds.PERF_VEHICLE_SPEED -> {
                val speed = value as Float
                _drivingStatus.value = checkDrivingStatus(speed)

                val newDetails = getDetailedCarData(speed)
                _vehicleDetails.value = LinkedHashMap(newDetails)

                Log.d("G70_Native", "속도: ${checkDrivingStatus(speed)}, 상시 데이터: ${_vehicleDetails.value}")
            }
            VehiclePropertyIds.GEAR_SELECTION -> {
                currentGear = value as Int
                checkDoorSafety()
            }
            289472775 -> {
                // 문 열림 상태
                isAnyDoorOpen = (value as? Array<*>)?.any { (it as? Int ?: 0) > 0 } ?: false
                checkDoorSafety()
            }
            VehiclePropertyIds.FUEL_LEVEL -> {
                val fuel = value as Float
                Log.d("G70_Native", checkFuelStatus(fuel))
                Log.d("G70_Native", "현재 주행 효율 등금: ${getEfficiencyGrade(fuel)}등급")
            }
            VehiclePropertyIds.NIGHT_MODE -> {
                val isNight = value as Boolean
                val lightCommand = changeHeadlight(isNight)

                if (lightCommand == 1) {
                    Log.d("G70_Native", "헤드라이트가 켜집니다.")
                } else {
                    Log.d("G70_Native", "헤드라이트가 꺼집니다.")
                }
            }
        }
    }

    // 문열림 위험 상황 판단
    private fun checkDoorSafety() {
        if (isHazardous(currentGear, isAnyDoorOpen)) {
            _drivingStatus.value = "위험! 주행 중 문 열림 감지!"
        }
    }

//    // 속도 주행 UI 변경 테스트 코드
//    init {
//        Handler(Looper.getMainLooper()).postDelayed({
//            val testResult = checkDrivingStatus(120f)
//            _drivingStatus.value = "테스트 중: $testResult"
//        }, 3000)
//    }

    fun setTemperature(temp: Float) {
        helper.setTemperature(temp)
    }

    fun setDoorLock(lock: Boolean) {
        helper.setDoorLock(lock)
        _isDoorLocked.value = lock
    }

    fun closeConnection() {
        helper.release()
    }
}