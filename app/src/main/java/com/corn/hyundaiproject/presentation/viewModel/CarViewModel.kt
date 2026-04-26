package com.corn.hyundaiproject.presentation.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.corn.hyundaiproject.data.repository.CarRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor (
    application: Application,
    private val repository: CarRepositoryImpl
) : AndroidViewModel(application) {

    val vehicleDetails: StateFlow<Map<String, String>> = repository.vehicleDetails
    val displayUnits: StateFlow<Map<String, String>> = repository.vehicleDetails

    fun refreshVehicleHealth() {
        // TODO: Repository를 통해 VHAL에 최신 상태 요청 명령 전송
    }

    override fun onCleared() {
        super.onCleared()
        // Helper → DataSource → Repository → MainViewModel → UI
        repository.closeConnection()
        Log.d("CarViewModel", "Car Service 연결 해제됨")
    }
}