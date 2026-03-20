package com.corn.hyundaiproject.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.corn.hyundaiproject.presentation.viewModel.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val hvacState by viewModel.hvacState.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(7f)
                .fillMaxHeight()
        ) {
            Text(
                text = "지도/내비 영역",
            )

            Button(
                onClick = {
                    viewModel.toggleWindow()
                }
            ) {
                Text(
                    text = "창문 제어"
                )
            }
        }
    }
}