package com.corn.hyundaiproject.presentation.screen

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.corn.hyundaiproject.presentation.viewModel.CarViewModel

@Composable
fun HvacScreen(
    carViewModel: CarViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {

}