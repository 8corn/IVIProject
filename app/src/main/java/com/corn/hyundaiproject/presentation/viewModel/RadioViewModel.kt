package com.corn.hyundaiproject.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RadioViewModel @Inject constructor() : ViewModel() {
    private val _currentFrequency = MutableStateFlow(87.5f)
    val currentFrequency: StateFlow<Float> = _currentFrequency.asStateFlow()

    private val _stationName = MutableStateFlow("БизнесFM")
    val stationName: StateFlow<String> = _stationName.asStateFlow()

    fun tuneUp() {
        viewModelScope.launch {
            if (_currentFrequency.value < 108.0f) {
                _currentFrequency.value = (_currentFrequency.value + 0.1f)
                updateStationName(_currentFrequency.value)
            }
        }
    }

    fun tuneDown() {
        viewModelScope.launch {
            if (_currentFrequency.value > 87.5f) {
                _currentFrequency.value = (_currentFrequency.value - 0.1f)
                updateStationName(_currentFrequency.value)
            }
        }
    }

    private fun updateStationName(freq: Float) {
        _stationName.value = when (String.format(Locale.getDefault(), "%.1f", freq)) {
            "87.5" -> "БизнесFM"
            "91.9" -> "현대 인포테인먼트 매거진"
            "107.7" -> "Genesis 가상 커넥트"
            else -> "가상 채널 (주파수 탐색 중)"
        }
    }
}