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

//                subscribeToTemperature()
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

//    // 차량 온도
//    private fun subscribeToTemperature() {
//        // 콜백 객체 생성 (차량의 온도가 바뀌면 이 람다가 실행되서 DataSource의 MutableStateFlow로 이동)
//        val callback = object : CarPropertyManager.CarPropertyEventCallback {
//            override fun onChangeEvent(value: CarPropertyValue<*>) {
//                val temp = value.value as? Float ?: 0f
//                Log.d("data/car/CarHelper", "차량 온도 변경됨: $temp")
//                onTemperatureChanged(temp)
//            }
//
//            override fun onErrorEvent(propId: Int, zone: Int) {
//                Log.d("data/car/CarHelper", "에러 발생: $propId")
//            }
//        }
//
//        // registerCallback 인자 위치 확인
//        propertyManager?.registerCallback(
//            callback,
//            VehiclePropertyIds.HVAC_TEMPERATURE_SET,
//            CarPropertyManager.SENSOR_RATE_ONCHANGE
//        )
//    }

    // 차량 데이터
    private fun subscribeToVehicleData() {
        val callback = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(value: CarPropertyValue<*>) {
//                when (value.propertyId) {
//                    // 위 차량 온도
//                    VehiclePropertyIds.HVAC_TEMPERATURE_SET -> {
//                        val temp = value.value as? Float ?: 0f
//                        Log.d("data/car/CarHelper", "차량 온도 변경됨: $temp")
//                    }
//
//                    // 외부 온도
//                    VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE -> {
//                        val outTemp = value.value as? Float ?: 0f
//                        Log.d("data/car/CarHelper", "외부 온도: $outTemp")
//                    }
//
//                    // 속도 확인 (하드웨어 문제로 현재 불가)
//                    VehiclePropertyIds.PERF_VEHICLE_SPEED -> {
//                        val speed = value.value as? Float ?: 0f
//                        Log.d("data/car/CarHelper", "실시간 속도: $speed km/h")
//                    }
//
//                    // 기어 확인 (하드웨어 문제로 현재 불가)
//                    VehiclePropertyIds.GEAR_SELECTION -> {
//                        val gear = value.value as? Int ?: 0
//                        Log.d("data/car/CarHelper", "현재 기어: $gear")
//                    }
//
//                    // 문 잠금
//                    289472773 -> {
//                        val isLocked = value.value as? Int ?: 0
//                        Log.d("data/car/CarHelper", "문 잠금 상태(1: 잠김, 0: 열림): $isLocked")
//                    }
//
//                    // 야간 모드
//                    VehiclePropertyIds.NIGHT_MODE -> {
//                        val isNight = value.value as? Boolean ?: false
//                        Log.d("data/car/CarHelper", "야간 모드 상태: $isNight")
//                    }
//
//                    // 연료 레벨
//                    VehiclePropertyIds.FUEL_LEVEL -> {
//                        val fuel = value.value as? Float ?: 0f
//                        Log.d("data/car/CarHelper", "연료 잔량: $fuel")
//                    }
//                }

                val propertyId = value.propertyId
                val v = value.value

                when (propertyId) {
                    VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE -> Log.d("data/car/CarHelper", "외부 온도: $v")
                    VehiclePropertyIds.PERF_VEHICLE_SPEED -> Log.d("data/car/CarHelper", "실시간 속도: $v km/h")
                    VehiclePropertyIds.GEAR_SELECTION -> Log.d("data/car/CarHelper", "현재 기어: $v")
                    289472773 -> Log.d("data/car/CarHelper", "문 잠금 상태(1: 잠김, 0: 열림): $v")
                    VehiclePropertyIds.NIGHT_MODE -> Log.d("data/car/CarHelper", "야간 모드 상태: $v")
                    VehiclePropertyIds.FUEL_LEVEL -> Log.d("data/car/CarHelper", "연료 잔량: $v")

                    // 조명 및 시동
                    289407235 -> Log.d("data/car/CarHelper", "헤드라이트: $v")
                    289408269 -> Log.d("data/car/CarHelper", "비상등 상태: $v")
                    339739916 -> Log.d("data/car/CarHelper", "시동 상태: $v")

                    // 창문 및 문 위치
                    289472780 -> Log.d("data/car/CarHelper", "창문 위치: $v")
                    289472775 -> Log.d("data/car/CarHelper", "문 열림 위치: $v")
                    291504390 -> Log.d("data/car/CarHelper", "연료 주입구 열림: $v")

                    // 공조 상세
                    356516106 -> Log.d("data/car/CarHelper", "팬 속도: $v")
                    356517135 -> Log.d("data/car/CarHelper", "핸들 열선: $v")
                    356517131 -> Log.d("data/car/CarHelper", "시트 온도: $v")

                    // 전기차일시 배터리 용량
                    291570965 -> Log.d("data/car/CarHelper", "EV 배터리: $v")
                }
            }

            override fun onErrorEvent(propertyId: Int, zone: Int) {
                Log.d("data/car/CarHelper", "에러 발생 Property ID: $propertyId")
            }
        }

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

        onchangeProps.forEach { id ->
            propertyManager?.registerCallback(
                callback,
                id,
                CarPropertyManager.SENSOR_RATE_ONCHANGE
            )
        }

        normalProps.forEach { id ->
            propertyManager?.registerCallback(
                callback,
                id,
                CarPropertyManager.SENSOR_RATE_NORMAL
            )
        }

//        // 내부 온도
//        propertyManager?.registerCallback(
//            callback,
//            VehiclePropertyIds.HVAC_TEMPERATURE_SET,
//            CarPropertyManager.SENSOR_RATE_ONCHANGE
//        )
//
//        // 외부 온도
//        propertyManager?.registerCallback(
//            callback,
//            VehiclePropertyIds.ENV_OUTSIDE_TEMPERATURE,
//            CarPropertyManager.SENSOR_RATE_NORMAL
//        )
//
//        // 속도 확인
//        propertyManager?.registerCallback(
//            callback,
//            VehiclePropertyIds.PERF_VEHICLE_SPEED,
//            CarPropertyManager.SENSOR_RATE_NORMAL
//        )
//
//        // 기어 상태 확인
//        propertyManager?.registerCallback(
//            callback,
//            VehiclePropertyIds.GEAR_SELECTION,
//            CarPropertyManager.SENSOR_RATE_ONCHANGE
//        )
//
//        // 문 잠금
//        propertyManager?.registerCallback(
//            callback,
//            289472773,
//            CarPropertyManager.SENSOR_RATE_ONCHANGE
//        )
//
//        // 야간 모드
//        propertyManager?.registerCallback(
//            callback,
//            VehiclePropertyIds.NIGHT_MODE,
//            CarPropertyManager.SENSOR_RATE_ONCHANGE
//        )
//
//        // 연료 레벨
//        propertyManager?.registerCallback(
//            callback,
//            VehiclePropertyIds.FUEL_LEVEL,
//            CarPropertyManager.SENSOR_RATE_NORMAL
//        )
    }
}