package com.corn.hyundaiproject.presentation.viewModel

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.corn.hyundaiproject.R
import com.corn.hyundaiproject.data.repository.CarRepositoryImpl
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
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
    val vehicleSpeed: Float = 0f,
)

private fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", mins, secs)
}

@HiltViewModel
class MediaViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val repository: CarRepositoryImpl
) : ViewModel() {
    private var exoPlayer: ExoPlayer? = null
    private var controllerFuture: ListenableFuture<MediaController>? = null
    private val mediaController: MediaController?
        get() = if (controllerFuture?.isDone == true) controllerFuture?.get() else null
    private val _mediaState = MutableStateFlow(MediaState())
    val mediaState: StateFlow<MediaState> = _mediaState.asStateFlow()

    init {
        setupCarApi()
        setupMediaController()
        updateProgress()
    }

    private fun setupMediaController() {
        viewModelScope.launch {
            try {
                val componentName = ComponentName(
                    "com.android.bluetooth",
                    "com.android.bluetooth.avrcpcontroller.BluetoothMediaBrowserService"
                )
                val sessionToken = try {
                    SessionToken(context, componentName)
                } catch (e: IllegalArgumentException) {
                    Log.e("MediaViewModel", "블루투스 서비스를 찾을 수 없습니다: ${e.message}")
                    null
                }

                if (sessionToken != null) {
                    controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
                    controllerFuture?.addListener({
                        val controller = mediaController ?: return@addListener

                        controller.addListener(object : Player.Listener {
                            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                                _mediaState.value = _mediaState.value.copy(
                                    title = mediaMetadata.title?.toString() ?: "Unknown",
                                    artist = mediaMetadata.artist?.toString() ?: "Unknown artist"
                                )
                            }

                            override fun onIsPlayingChanged(isPlaying: Boolean) {
                                _mediaState.value = _mediaState.value.copy(isPlaying = isPlaying)
                            }
                        })
                    }, MoreExecutors.directExecutor())
                } else {
                    Log.d("MediaViewModel", "내장 ExoPlayer 가동 시작")
                    setupPlayer()
                }
            } catch (e: Exception) {
                Log.e("MediaViewModel", "MediaController 설정 중 예상치 못한 오류: ${e.message}")
                setupPlayer()
            }
        }
    }


    @OptIn(UnstableApi::class)
    private fun setupPlayer() {
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            val items = listOf(
                createMediaItem(R.raw.bad_day, "Bad Day", "Daniel Powter"),
                createMediaItem(R.raw.congrats, "Congrats", "LANY"),
                createMediaItem(R.raw.gone_gone_gone, "Gone, Gone, Gone", "Phillip Phillips")
            )

            setMediaItems(items)
            prepare()

            addListener(object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    mediaItem?.mediaMetadata?.let { metadata ->
                        _mediaState.value = _mediaState.value.copy(
                            title = metadata.title?.toString() ?: "Unknown",
                            artist = metadata.artist?.toString() ?: "Unknown artist"
                        )
                    }
                }
            })
        }
    }

    private fun setupCarApi() {
        viewModelScope.launch {
            launch {
                repository.vehicleDetails.collect { details ->
                    val speed = details["speed"]?.toFloatOrNull() ?: 0f
                    val rpm = details["rpm"]?.toFloatOrNull() ?: 0f

                    _mediaState.value = _mediaState.value.copy(vehicleSpeed = speed)

                    Log.d("MediaViewModel", "데이터 수신 - 속도: $speed, RPM: $rpm")
                }
            }

            launch {
                repository.drivingStatus.collect { status ->
                    Log.d("MediaViewModel", "현재 주행 상태: $status")
                }
            }
        }
    }

    private fun updateProgress() {
        viewModelScope.launch {
            while (true) {
                val activePlayer: Player? = mediaController ?: exoPlayer

                activePlayer?.let { player ->
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

    fun togglePlay() {
        if (mediaController != null) {
            mediaController?.let {
                if (it.isPlaying) it.pause() else it.play()
            }
        } else {
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
    }

    override fun onCleared() {
        super.onCleared()

        controllerFuture?.let {
            MediaController.releaseFuture(it)
        }

        exoPlayer?.let {
            it.stop()
            it.release()
        }

        repository.closeConnection()
    }

    @OptIn(UnstableApi::class)
    private fun createMediaItem(rawId: Int, title: String, artist: String): MediaItem {
        val uri = RawResourceDataSource.buildRawResourceUri(rawId)
        return MediaItem.Builder()
            .setUri(uri)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(title)
                    .setArtist(artist)
                    .build()
            )
            .build()
    }

    fun skipToNext() {
        if (mediaController != null) mediaController?.seekToNext() else exoPlayer?.seekToNext()
    }

    fun skipToPrepare() {
        if (mediaController != null) mediaController?.seekToPrevious() else exoPlayer?.seekToPrevious()
    }
}