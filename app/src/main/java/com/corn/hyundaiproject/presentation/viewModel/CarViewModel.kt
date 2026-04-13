package com.corn.hyundaiproject.presentation.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.corn.hyundaiproject.data.repository.CarRepositoryImpl

class CarViewModel (
    application: Application,
    private val repository: CarRepositoryImpl
) : AndroidViewModel(application) {

    override fun onCleared() {
        super.onCleared()

        // Helper → DataSource → Repository → MainViewModel → UI
        repository.releaseResources()
        Log.d("CarViewModel", "Car Service 연결 해제됨")
    }
}