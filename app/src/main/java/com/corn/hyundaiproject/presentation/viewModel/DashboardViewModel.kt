package com.corn.hyundaiproject.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {
    private val _speed = MutableStateFlow(0)
    val speed: StateFlow<Int> = _speed.asStateFlow()

    private val _rpm = MutableStateFlow(0f)
    val rpm: StateFlow<Float> = _rpm.asStateFlow()

    init {
        simulateDriving()
    }

    private fun simulateDriving() {
        viewModelScope.launch {
            var targetSpeed = 0
            var targetRpm = 800f

            while (true) {
                targetSpeed = (targetSpeed + (1..3).random()).coerceAtMost(300)
                targetRpm = if (targetSpeed % 40 == 0) 1500f else (targetRpm + (10..50).random()).coerceAtMost(7000f)

                _speed.value = targetSpeed
                _rpm.value = targetRpm
                delay(100)
            }
        }
    }
}