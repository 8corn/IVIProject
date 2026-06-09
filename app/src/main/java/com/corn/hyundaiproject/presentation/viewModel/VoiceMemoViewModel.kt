package com.corn.hyundaiproject.presentation.viewModel

import android.app.Application
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class VoiceMemoViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    private val context get() = getApplication<Application>().applicationContext
    private var mediaRecord: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var currentRecordingFile: File? = null

    var isRecording = mutableStateOf(false)
    var isPlaying = mutableStateOf(false)
    var recordingDuration = mutableIntStateOf(0)
    val savedMemos = mutableStateListOf<File>()

    private var timerJob: Job? = null

    init {
        loadSavedMemos()
    }

    fun loadSavedMemos() {
        savedMemos.clear()
        val recordDir = context.getExternalFilesDir("Memos")
        recordDir?.listFiles()?.filter { it.extension == "mp4" }?.sortedByDescending { it.lastModified() }?.let {
            savedMemos.addAll(it)
        }
    }

    fun startRecording() {
        if (isRecording.value) return

        val recordDir = context.getExternalFilesDir("Memos") ?: return
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        currentRecordingFile = File(recordDir, "음성메모_$timeStamp.mp4")

        mediaRecord = MediaRecorder(context).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(currentRecordingFile?.absolutePath)
            prepare()
            start()
        }

        isRecording.value = true
        startTimer()
    }

    fun stopRecording() {
        if (!isRecording.value) return

        timerJob?.cancel()
        try {
            mediaRecord?.apply {
                stop()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaRecord = null
        isRecording.value = false
        recordingDuration.intValue = 0

        loadSavedMemos()
    }

    fun playMemo(file: File) {
        if (isPlaying.value) {
            stopPlaying()
        }

        mediaPlayer = MediaPlayer().apply {
            setDataSource(file.absolutePath)
            prepare()
            start()
            this@VoiceMemoViewModel.isPlaying.value = true
            setOnCompletionListener {
                stopPlaying()
            }
        }
    }

    fun stopPlaying() {
        try {
            mediaPlayer?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaPlayer = null
        isPlaying.value = false
    }

    private fun startTimer() {
        recordingDuration.intValue = 0
        timerJob = viewModelScope.launch {
            while (isRecording.value) {
                delay(1000)
                recordingDuration.intValue++
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaRecord?.release()
        mediaPlayer?.release()
    }
}