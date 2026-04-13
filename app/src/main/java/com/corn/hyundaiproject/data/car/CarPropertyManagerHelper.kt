package com.corn.hyundaiproject.data.car

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log

class CarPropertyManagerHelper(
    context: Context,
    private val onTemperatureChanged: (Float) -> Unit
) {
    private var car: Car? = null
    private var propertyManager: CarPropertyManager? = null

    // 콜백을 클래스 멤버 변수로 선언
    private val propertyCallback = object : CarPropertyManager.CarPropertyEventCallback {
        override fun onChangeEvent(value: CarPropertyValue<*>) {

            val propertyId = value.propertyId
            val rawValue = value.value

            val displayValue =
                when (rawValue) {
                    is Array<*> -> rawValue.contentDeepToString()
                    is IntArray -> rawValue.contentToString()
                    is FloatArray -> rawValue.contentToString()
                    else -> rawValue
                }

            Log.d("data/car/CarHelper", "속성 변경됨 [ID: $propertyId]: $displayValue")

            when (propertyId) {
                VehiclePropertyIds.HVAC_TEMPERATURE_SET -> {
                    val temp = rawValue as? Float ?: 0f
                    onTemperatureChanged(temp)
                    Log.d("data/car/CarHelper", "설정 온도: $temp")
                }
                VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE -> Log.d("data/car/CarHelper", "외부 온도: $rawValue")
                VehiclePropertyIds.PERF_VEHICLE_SPEED -> Log.d("data/car/CarHelper", "실시간 속도: $rawValue km/h")
                VehiclePropertyIds.GEAR_SELECTION -> Log.d("data/car/CarHelper", "현재 기어: $rawValue")
                VehiclePropertyIds.NIGHT_MODE -> Log.d("data/car/CarHelper", "야간 모드 상태: $rawValue")
                VehiclePropertyIds.FUEL_LEVEL -> Log.d("data/car/CarHelper", "연료 잔량: $rawValue")

                // 조명 및 시동
                289407235 -> Log.d("data/car/CarHelper", "헤드라이트: $rawValue")
                289408269 -> Log.d("data/car/CarHelper", "비상등 상태: $rawValue")
                339739916 -> Log.d("data/car/CarHelper", "시동 상태: $rawValue")

                // 창문 및 문 위치
                289472773 -> {
                    val data = rawValue as? Array<*>
                    val lockStatus = data?.get(0) as? Int?: 0

                    if (lockStatus == 1) {
                        Log.d("data/car/CarHelper", "차 문이 잠겼습니다.")
                    } else {
                        Log.d("data/car/CarHelper", "차 문이 열렸습니다.")
                    }
                }
                289472780 -> {
                    val data = (rawValue as? Array<*>)?.contentDeepToString() ?: rawValue.toString()
                    Log.d("data/car/CarHelper", "창문 위치: $data")
                }
                289472775 -> {
                    val data = (rawValue as? Array<*>)?.contentDeepToString() ?: rawValue.toString()
                    Log.d("data/car/CarHelper", "문 열림 위치: $data")
                }
                291504390 -> Log.d("data/car/CarHelper", "연료 주입구 열림: $rawValue")

                // 공조 상세
                356516106 -> Log.d("data/car/CarHelper", "팬 속도: $rawValue")
                356517135 -> Log.d("data/car/CarHelper", "핸들 열선: $rawValue")
                356517131 -> Log.d("data/car/CarHelper", "시트 온도: $rawValue")

                // 전기차일시 배터리 용량
                291570965 -> {
                    val data = (rawValue as? Array<*>)?.contentDeepToString() ?: rawValue.toString()
                    Log.d("data/car/CarHelper", "EV 배터리: $data")
                }
            }
        }

        override fun onErrorEvent(propertyId: Int, zone: Int) {
            Log.d("data/car/CarHelper", "에러 발생 Property ID: $propertyId, Zone: $zone")
        }
    }

    // ServiceConnection 인터페이스를 직접 구현

    // ServiceConnection: 안드로이드에서 다른 프로세스(자동차 시스템 서비스 등)와 통신할 때 사용하는 기본적인 방식
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            try {
                // 서비스 연결 성공 시 매니저 획득

                // getManager 결과가 null일 수도 있으니 as?로 처리
                propertyManager = car?.getCarManager(Car.PROPERTY_SERVICE) as? CarPropertyManager

                val configs = propertyManager?.propertyList
//                configs?.forEach { config ->
//                    Log.d("data/car/CarHelper", "지원되는 차량 속성: ${config.propertyId}")
//                }
                val isSupported = configs?.any {it.propertyId == VehiclePropertyIds.HVAC_TEMPERATURE_SET }
                Log.d("data/car/CarHelper", "HVAC 온도 제어 지원 여부: $isSupported")

                subscribeToVehicleData()

                Log.d("data/car/CarHelper", "Car Service Connected")
            } catch (e: Exception) {
                Log.e("data/car/CarHelper", "Manager 획득 실패: ${e.message}")
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            propertyManager = null
            Log.d("data/car/CarHelper", "Car Service Disconnected")
        }
    }

    init {
        car = Car.createCar(context, connection)

        // createCar 호출 후 반드시 connect() 를 해줘야 하는 버전
        car?.connect()
    }

    // 차량 데이터
    private fun subscribeToVehicleData() {
        val configs = propertyManager?.propertyList ?: emptyList()
        val available = configs.map { it.propertyId }

        // SENSOR_RATE_ONCHANGE -> 값이 변할 때만 값을 던져줌
        // SENSOR_RATE_NORMAL -> 주기적으로 값이 변하든 안 변하든 일정 시간 간격으로 계속 값을 던져줌

        val onchangeProps = listOf(
            VehiclePropertyIds.HVAC_TEMPERATURE_SET,
            VehiclePropertyIds.GEAR_SELECTION,
            VehiclePropertyIds.NIGHT_MODE,
            289407235,
            289408269,
            339739916,
            289472780,
            289472773,
            289472775,
            356517135
        )
        val normalProps = listOf(
            VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE,
            VehiclePropertyIds.PERF_VEHICLE_SPEED,
            VehiclePropertyIds.FUEL_LEVEL,
            291504390,
            356516106,
            356517131,
            291570965
        )

        // 지원되는 속성만 필터링해서 등록
        fun registerSafety(idList: List<Int>, rate: Int) {
            idList.forEach { id ->
                if (available.contains(id)) {
                    val config = propertyManager?.getCarPropertyConfig(id)
                    val areaIds = config?.areaIds ?: intArrayOf(0)

                    areaIds.forEach { areaId ->
                        val success = propertyManager?.registerCallback(
                            propertyCallback,
                            id,
                            rate.toFloat()
                        )
                        Log.d("data/car/CarHelper", "ID $id (Area $areaId) 등록 결과: $success")
                    }
                } else {
                    Log.w("data/car/CarHelper", "지원되지 않는 속성 무시됨: $id")
                }
            }
        }

        registerSafety(
            onchangeProps,
            CarPropertyManager.SENSOR_RATE_ONCHANGE.toInt()
        )

        registerSafety(
            normalProps,
            CarPropertyManager.SENSOR_RATE_NORMAL.toInt()
        )
    }

    fun release() {
        try {
            propertyManager?.unregisterCallback(propertyCallback)
            if (car?.isConnected == true) {
                car?.disconnect()
            }
            car = null
            propertyManager = null
            Log.d("data/car/CarHelper", "성공적으로 카 서비스를 릴리즈 하였습니다.")
        } catch (e: Exception) {
            Log.e("data/car/CarHelper", "릴리즈 실패: ${e.message}")
        }
    }
}