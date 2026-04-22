package com.corn.hyundaiproject.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.corn.hyundaiproject.data.car.CarPropertyDataSource
import com.corn.hyundaiproject.data.repository.CarRepositoryImpl
import com.corn.hyundaiproject.domain.usecase.GetTemperatureUseCase
import com.corn.hyundaiproject.presentation.ui.theme.HyundaiProjectTheme
import com.corn.hyundaiproject.presentation.viewModel.CarViewModel
import com.corn.hyundaiproject.presentation.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HyundaiProjectTheme {
                // hiltViewModel()이 자동으로 Factory를 만들어서 뷰모델을 생성해줌
                val mainViewModel: MainViewModel = hiltViewModel()
                val carViewModel: CarViewModel = hiltViewModel()
                MainScreen(
                    mainViewModel = mainViewModel,
                    carViewModel = carViewModel
                )
            }
        }
    }
}