package com.corn.hyundaiproject.presentation.viewModel

import android.content.Context
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import com.corn.hyundaiproject.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
class MediaViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ViewModel() {
    private var exoPlayer: ExoPlayer? = null
    private val _mediaState = MutableStateFlow(MediaState())
    val mediaState: StateFlow<MediaState> = _mediaState.asStateFlow()

    init {
        setupPlayer()
        updateProgress()
    }

    @OptIn(UnstableApi::class)
    private fun setupPlayer() {
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.bad_day))
            setMediaItem(mediaItem)
            prepare()
        }
    }

    private fun updateProgress() {
        viewModelScope.launch {
            while (true) {
                exoPlayer?.let { player ->
                    val currentPos = player.currentPosition
                    val duration = player.duration.coerceAtLeast(10L)

                    if (duration > 0) {
                        val progress = currentPos.toFloat() / duration.toFloat()

                        _mediaState.value = _mediaState.value.copy(
                            progress = progress,
                            currentTime = formatTime((currentPos / 1000).toInt()),
                            totalTime = formatTime((duration / 1000).toInt()),
                            isPlaying = player.isPlaying
                        )
                    }
                }
                delay(500)
            }
        }
    }

    fun skipForward() {
        exoPlayer?.let {
            val seekPos = (it.currentPosition + 10000).coerceAtMost(it.duration)
            it.seekTo(seekPos)
        }
    }

    fun skipBackward() {
        exoPlayer?.let {
            val seekPos = (it.currentPosition - 10000).coerceAtLeast(0L)
            it.seekTo(seekPos)
        }
    }

    fun togglePlay() {
        exoPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _mediaState.value = _mediaState.value.copy(isPlaying = false)
            } else {
                it.play()
                _mediaState.value = _mediaState.value.copy(isPlaying = true)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer?.release()
        exoPlayer = null
    }
}