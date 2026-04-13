package com.corn.hyundaiproject.data.car

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CarPropertyDataSource(context: Context) {

    // 실시간 온도를 담는 StateFlow (Helper에서 온 데이터로 업데이트)
    private val _temperature = MutableStateFlow(22f)
    val temperature: StateFlow<Float> = _temperature.asStateFlow()

    // Helper를 생성하면서 콜백(람다)을 전달
    // Helper에서 onTemperatureChanged(temp)를 호출하면 이 블록이 실행됨
    private val helper = CarPropertyManagerHelper(context) { newTemp ->
        _temperature.value = newTemp
    }

    fun closeConnection() {
        helper.release()
    }
}