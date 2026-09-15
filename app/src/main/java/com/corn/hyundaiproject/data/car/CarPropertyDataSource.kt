package com.corn.hyundaiproject.data.car

import android.car.VehiclePropertyIds
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarPropertyDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    // C++ 라이브러리를 메모리에 로드
    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    init {
        val handler = Handler(Looper.getMainLooper())
        var fakeTemp = 25f
        var fakeOutsideTemp = 20f
        var increasing = true

        handler.post(object : Runnable {
            override fun run() {
                if (increasing) {
                    fakeTemp += 1.0f
                    fakeOutsideTemp += 0.2f
                } else {
                    fakeTemp -= 1.0f
                    fakeOutsideTemp -= 0.2f
                }
                
                if (fakeTemp >= 35f) increasing = false
                if (fakeTemp <= 25f) increasing = true

                _temperature.value = fakeTemp
                _outsideTemperature.value = fakeOutsideTemp

                val advice = getClimateAdvice(fakeOutsideTemp, fakeTemp)
                _climateAdvice.value = advice

                Log.d("G70_VHAL_TEST", "현재 온도: $fakeTemp, 실외: $fakeOutsideTemp, C++: $advice")

                handler.postDelayed(this, 1000)
            }
        })
    }

    // C++에 있는 함수를 정의 -> 함수 이름이 c++ 파일의 이름과 동일해야 함
    private external fun getEfficiencyGrade(fuelEfficiency: Float): Int
    private external fun checkDrivingStatus(speed: Float): String
    private external fun changeHeadlight(night: Boolean): Int
    private external fun getClimateAdvice(exteriorTemp: Float, interiorTemp: Float): String
    private external fun checkFuelStatus(fuelLevel: Float): String
    private external fun isHazardous(gear: Int, isDoorOpen: Boolean): Boolean
    private external fun getDetailedCarData(speed: Float): Map<String, String>
    private external fun getAdasDistanceNative(rawDistanceData: Float): Float
    private external fun tuneRadioNative(currentFrequency: Float, isTuneUp: Boolean): Map<String, String>

    // 상태 저장용 변수 (상태 판단을 위해 필요)
    private var currentGear: Int = 0
    private var isAnyDoorOpen: Boolean = false

    // 실시간 온도를 담는 StateFlow
    private val _drivingStatus = MutableStateFlow("상태 파악 중...")
    val drivingStatus: StateFlow<String> = _drivingStatus.asStateFlow()

    private val _temperature = MutableStateFlow(22f)
    val temperature: StateFlow<Float> = _temperature.asStateFlow()

    private val _outsideTemperature = MutableStateFlow(20f)
    val outsideTemperature: StateFlow<Float> = _outsideTemperature.asStateFlow()

    private val _climateAdvice = MutableStateFlow("쾌적합니다.")
    val climateAdvice: StateFlow<String> = _climateAdvice.asStateFlow()

    private val _isDoorLocked = MutableStateFlow(true)
    val isDoorLocked: StateFlow<Boolean> = _isDoorLocked.asStateFlow()

    private val _isWindowOpen = MutableStateFlow(true)
    val isWindowOpen: StateFlow<Boolean> = _isWindowOpen.asStateFlow()

    private val _vehicleDetails = MutableStateFlow(
        mapOf("speed" to "0", "rpm" to "0", "drive_mode" to "NORMAL", "engine_temp" to "90.5")
    )
    val vehicleDetails: StateFlow<Map<String, String>> = _vehicleDetails.asStateFlow()

    private val _forwardDistance = MutableStateFlow(50f)
    val forwardDistance: StateFlow<Float> = _forwardDistance.asStateFlow()

    private val _isLaneDeparture = MutableStateFlow(false)
    val isLaneDeparture: StateFlow<Boolean> = _isLaneDeparture.asStateFlow()

    private val _fuelLevel = MutableStateFlow(60f)
    val fuelLevel: StateFlow<Float> = _fuelLevel.asStateFlow()

    private val _radioFrequency = MutableStateFlow(87.5f)
    val radioFrequency: StateFlow<Float> = _radioFrequency.asStateFlow()

    private val _radioStationName = MutableStateFlow("БизнесFM")
    val radioStationName: StateFlow<String> = _radioStationName.asStateFlow()

    // Helper를 생성하면서 콜백(람다)을 전달
    private val helper = CarPropertyManagerHelper(context) { propertyId, value ->
        when (propertyId) {
            VehiclePropertyIds.HVAC_TEMPERATURE_SET -> {
                val temp = value as Float
                _temperature.value = temp
                _climateAdvice.value = getClimateAdvice(_outsideTemperature.value, temp)
            }
            VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE -> {
                val temp = value as Float
                _outsideTemperature.value = temp
                _climateAdvice.value = getClimateAdvice(temp, _temperature.value)
            }
            VehiclePropertyIds.PERF_VEHICLE_SPEED -> {
                val speed = value as Float
                _drivingStatus.value = checkDrivingStatus(speed)
                val newDetails = getDetailedCarData(speed)
                _vehicleDetails.value = LinkedHashMap(newDetails)
                _forwardDistance.value = (100f - speed).coerceAtLeast(10f)
                _isLaneDeparture.value = speed > 120f
            }
            VehiclePropertyIds.GEAR_SELECTION -> {
                currentGear = value as Int
                checkDoorSafety()
            }
            289472775 -> {
                isAnyDoorOpen = (value as? Array<*>)?.any { (it as? Int ?: 0) > 0 } ?: false
                checkDoorSafety()
            }
            289472773 -> {
                val locked = (value as? Int) == 1
                _isDoorLocked.value = locked
            }
            VehiclePropertyIds.FUEL_LEVEL -> {
                val fuel = value as Float
                _fuelLevel.value = fuel
            }
            VehiclePropertyIds.NIGHT_MODE -> {
                val isNight = value as Boolean
                changeHeadlight(isNight)
            }
            VehiclePropertyIds.WINDOW_POS -> {
                val isOpen = value as? Boolean ?: false
                _isWindowOpen.value = isOpen
            }
            0x21400101 -> {
                val distance = value as? Float ?: 0f
                _forwardDistance.value = getAdasDistanceNative(distance)
            }
            0x21400102 -> {
                val departure = value as? Int ?: 0
                _isLaneDeparture.value = (getAdasDistanceNative(departure.toFloat()) == 0.5f)
            }
        }
    }

    suspend fun fetchLatestFromVhal() = withContext(Dispatchers.IO) {
        try {
            val currentSpeed = _vehicleDetails.value["speed"]?.toFloatOrNull() ?: 0f
            val refreshedDetails = getDetailedCarData(currentSpeed)
            val manager = helper.getManager()
            val tempRaw = manager?.getProperty<Any>(VehiclePropertyIds.HVAC_TEMPERATURE_SET, 0)?.value
            val tempValue = when(tempRaw) {
                is Float -> tempRaw.toString()
                is Int -> tempRaw.toFloat().toString()
                else -> refreshedDetails["engine_temp"] ?: _vehicleDetails.value["engine_temp"] ?: "90.5"
            }
            _vehicleDetails.value = mapOf(
                "model" to (refreshedDetails["model"] ?: _vehicleDetails.value["model"] ?: "G70 Sport"),
                "vin" to (refreshedDetails["vin"] ?: _vehicleDetails.value["vin"] ?: "KMH-G70-2026-XXXX"),
                "engine_temp" to tempValue,
                "drive_mode" to (refreshedDetails["drive_mode"] ?: _vehicleDetails.value["drive_mode"] ?: "NORMAL"),
                "speed" to currentSpeed.toInt().toString(),
                "rpm" to (refreshedDetails["rpm"] ?: _vehicleDetails.value["rpm"] ?: "0")
            )
        } catch (e: Exception) {
            Log.e("CarPropertyDataSource", "VHAL 데이터 강제 수집 중 에러 발생", e)
        }
    }

    private fun checkDoorSafety() {
        if (isHazardous(currentGear, isAnyDoorOpen)) {
            _drivingStatus.value = "위험! 주행 중 문 열림 감지!"
        }
    }

    fun requestRadioTune(isTuneUp: Boolean) {
        val details = tuneRadioNative(_radioFrequency.value, isTuneUp)
        _radioFrequency.value = details["frequency"]?.toFloatOrNull() ?: 87.5f
        _radioStationName.value = details["station_name"] ?: "알 수 없는 채널"
    }

    fun setTemperature(temp: Float) = helper.setTemperature(temp)
    fun setDoorLock(lock: Boolean) {
        helper.setDoorLock(lock)
        _isDoorLocked.value = lock
    }
    fun setWindowPosition(isOpen: Boolean, areaId: Int) {
        helper.setWindowPosition(isOpen, areaId)
        _isWindowOpen.value = isOpen
    }
    fun closeConnection() = helper.release()
}