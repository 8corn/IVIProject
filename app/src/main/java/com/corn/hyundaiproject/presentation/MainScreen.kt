package com.corn.hyundaiproject.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.corn.hyundaiproject.presentation.viewModel.CarViewModel
import com.corn.hyundaiproject.presentation.viewModel.MainViewModel

@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    carViewModel: CarViewModel,
) {
    val hvacState by mainViewModel.hvacState.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(7f)
                .fillMaxHeight()
        ) {
            Column (
                modifier = Modifier
            ) {
                Text(
                    text = "지도/내비 영역",
                    )

                Text(
                    text = "현재 설정 온도: ${hvacState.temperature}°C",
                    style = TextStyle(
                        fontSize = 30.sp
                    )
                )

                Button(
                    onClick = {
                        mainViewModel.toggleWindow()
                    }
                ) {
                    Text(
                        text = "창문 제어"
                    )
                }
            }
        }
    }
}