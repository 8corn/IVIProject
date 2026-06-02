package com.corn.hyundaiproject.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.corn.hyundaiproject.data.car.CarPropertyDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RadioViewModel @Inject constructor(
    private val carPropertyDataSource: CarPropertyDataSource
) : ViewModel() {
    val currentFrequency: StateFlow<Float> = carPropertyDataSource.radioFrequency

    val stationName: StateFlow<String> = carPropertyDataSource.radioStationName

    fun tuneUp() {
        carPropertyDataSource.requestRadioTune(isTuneUp = true)
    }

    fun tuneDown() {
        carPropertyDataSource.requestRadioTune(isTuneUp = false)
    }
}