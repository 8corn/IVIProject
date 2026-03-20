package com.corn.hyundaiproject.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.corn.hyundaiproject.data.car.CarPropertyDataSource
import com.corn.hyundaiproject.data.repository.CarRepositoryImpl
import com.corn.hyundaiproject.presentation.ui.theme.HyundaiProjectTheme
import com.corn.hyundaiproject.presentation.viewModel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 수동으로 의존성 조립 (hilt 없을 때 사용하는 방식)
        val dataSource = CarPropertyDataSource(applicationContext)
        val repository = CarRepositoryImpl(dataSource)

        // ViewModel 생성 (Factory 사용)
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(repository) as T
            }
        })[MainViewModel::class.java]

        setContent {
            HyundaiProjectTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}