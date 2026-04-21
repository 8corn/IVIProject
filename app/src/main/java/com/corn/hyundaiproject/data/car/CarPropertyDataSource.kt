package com.corn.hyundaiproject.data.car

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.content.Context
import android.os.Handler
import android.os.Looper
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
    private external fun getNativeMessage(): String
    private external fun getEfficiencyGrade(fuelEfficiency: Float): Int
    private external fun checkDrivingStatus(speed: Float): String

    private val _drivingStatus = MutableStateFlow("상태 파악 중...")
    val drivingStatus: StateFlow<String> = _drivingStatus.asStateFlow()

    // 실시간 온도를 담는 StateFlow (Helper에서 온 데이터로 UI 업데이트)
    private val _temperature = MutableStateFlow(22f)
    val temperature: StateFlow<Float> = _temperature.asStateFlow()

    // Helper를 생성하면서 콜백(람다)을 전달
    // Helper에서 onTemperatureChanged(temp)를 호출하면 이 블록이 실행됨
    private val helper = CarPropertyManagerHelper(context) { propertyId, value ->
        when (propertyId) {
            VehiclePropertyIds.HVAC_TEMPERATURE_SET -> {
                _temperature.value = value as Float
            }

            VehiclePropertyIds.PERF_VEHICLE_SPEED -> {
                val speed = value as Float
                val result = checkDrivingStatus(speed)
                _drivingStatus.value = result
                Log.d("G70_Native", "C++ 엔진 판다: $result")
            }

            VehiclePropertyIds.FUEL_LEVEL -> {
                val fuel = value as Float
                val grade = getEfficiencyGrade(fuel)
                Log.d("G70_Native", "현재 주행 효율 등금: ${grade}등급")
            }
        }
    }

    // 속도 주행 UI 변경 테스트 코드
    init {
        Handler(Looper.getMainLooper()).postDelayed({
            val testResult = checkDrivingStatus(120f)
            _drivingStatus.value = "테스트 중: $testResult"
        }, 3000)
    }

    fun setTemperature(temp: Float) {
        helper.setTemperature(temp)
    }

    fun setDoorLock(lock: Boolean) = helper.setDoorLock(lock)

    fun closeConnection() {
        helper.release()
    }
}