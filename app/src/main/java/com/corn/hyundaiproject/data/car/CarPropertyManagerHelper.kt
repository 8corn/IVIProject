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
                propertyManager = car?.getCarManager(CarPropertyManager::class.java)
                subscribeToTemperature()
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

    // 차량 온도
    private fun subscribeToTemperature() {
        // 콜백 객체 생성 (차량의 온도가 바뀌면 이 람다가 실행되서 DataSource의 MutableStateFlow로 이동)
        val callback = object : CarPropertyManager.CarPropertyEventCallback {
            override fun onChangeEvent(value: CarPropertyValue<*>) {
                val temp = value.value as? Float ?: 0f
                Log.d("data/car/CarHelper", "온도 변경됨: $temp")
                onTemperatureChanged(temp)
            }

            override fun onErrorEvent(propId: Int, zone: Int) {
                Log.d("data/car/CarHelper", "에러 발생: $propId")
            }
        }

        // registerCallback 인자 위치 확인
        propertyManager?.registerCallback(
            callback,
            VehiclePropertyIds.HVAC_TEMPERATURE_SET,
            CarPropertyManager.SENSOR_RATE_ONCHANGE
        )
    }
}