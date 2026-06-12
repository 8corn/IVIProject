package com.corn.hyundaiproject.presentation.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.corn.hyundaiproject.data.repository.CarRepositoryImpl
import com.corn.hyundaiproject.domain.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor (
    application: Application,
    private val repository: CarRepository
) : AndroidViewModel(application) {

    val vehicleDetails: StateFlow<Map<String, String>> = repository.vehicleDetails
    val displayUnits: StateFlow<Map<String, String>> = repository.vehicleDetails

    fun refreshVehicleHealth() {
        viewModelScope.launch {
            try {
                Log.d("CarViewModel", "VHAL에 최신 차량 상태 동기화 요청 시작")

                repository.fetchLatestVehicleDetails()

                Log.d("CarViewModel", "VHAL 데이터 동기화 완료")
            } catch (e: Exception) {
                Log.e("CarViewModel", "차량 상태 갱신 실패", e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Helper → DataSource → Repository → MainViewModel → UI
        repository.closeConnection()
        Log.d("CarViewModel", "Car Service 연결 해제됨")
    }
}