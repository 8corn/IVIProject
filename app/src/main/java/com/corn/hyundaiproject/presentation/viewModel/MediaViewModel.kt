package com.corn.hyundaiproject.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

data class MediaState(
    val title: String = "G70 Engine Sound",
    val artist: String = "Genesis Sports+",
    val isPlaying: Boolean = false,
    val progress: Float = 0.3f,
    val currentTime: String = "01:24",
    val totalTime: String = "03:45",
)

private fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", mins, secs)
}

@HiltViewModel
class MediaViewModel @Inject constructor() : ViewModel() {
    private val _mediaState = MutableStateFlow(MediaState())
    val mediaState: StateFlow<MediaState> = _mediaState.asStateFlow()

    private val totalDurationSeconds = 225

    init {
        viewModelScope.launch {
            while (true) {
                val currentState = _mediaState.value

                if (currentState.isPlaying && currentState.progress < 1.0f) {
                    val nextProgress = (currentState.progress + 0.005f).coerceAtMost(1.0f)
                    val currentSecs = (totalDurationSeconds * nextProgress).toInt()

                    _mediaState.value = currentState.copy(
                        progress = nextProgress,
                        currentTime = formatTime(currentSecs)
                    )
                } else {
                    moveToNextSong()
                }
                delay(1000)
            }
        }
    }

    fun skipForward() {
        val newProgress = (_mediaState.value.progress + 0.05f).coerceAtMost(1f)
        val currentSecs = (totalDurationSeconds * newProgress).toInt()
        _mediaState.value = _mediaState.value.copy(
            progress = newProgress,
            currentTime = formatTime(currentSecs)
        )
    }

    fun skipBackward() {
        val newProgress = (_mediaState.value.progress - 0.05f).coerceAtLeast(0f)
        val currentSecs = (totalDurationSeconds * newProgress).toInt()
        _mediaState.value = _mediaState.value.copy(
            progress = newProgress,
            currentTime = formatTime(currentSecs)
        )
    }

    fun togglePlay() {
        _mediaState.value = _mediaState.value.copy(
            isPlaying = !_mediaState.value.isPlaying
        )
    }

    private fun moveToNextSong() {
        _mediaState.value = _mediaState.value.copy(
            title = "Next Sport+ Track",
            progress = 0f,
            currentTime = "00:00",
            isPlaying = true
        )
    }
}