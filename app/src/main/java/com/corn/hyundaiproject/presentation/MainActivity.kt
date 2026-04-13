package com.corn.hyundaiproject.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.corn.hyundaiproject.data.car.CarPropertyDataSource
import com.corn.hyundaiproject.data.repository.CarRepositoryImpl
import com.corn.hyundaiproject.presentation.ui.theme.HyundaiProjectTheme
import com.corn.hyundaiproject.presentation.viewModel.CarViewModel
import com.corn.hyundaiproject.presentation.viewModel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 수동으로 의존성 조립 (hilt 없을 때 사용하는 방식)
        val dataSource = CarPropertyDataSource(applicationContext)
        val repository = CarRepositoryImpl(dataSource)

        // repository를 가져오기 위해(Hilt가 도입되면 사라질 코드)
        @Suppress("UNCHECKED_CAST")
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return when {
                    modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repository) as T
                    modelClass.isAssignableFrom(CarViewModel::class.java) -> CarViewModel(application, repository) as T
                    else -> throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }

        val mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        val carViewModel = ViewModelProvider(this, factory)[CarViewModel::class.java]

        setContent {
            HyundaiProjectTheme {
                MainScreen(
                    mainViewModel = mainViewModel,
                    carViewModel = carViewModel
                )
            }
        }
    }
}