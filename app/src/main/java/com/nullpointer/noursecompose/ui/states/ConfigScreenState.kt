package com.nullpointer.noursecompose.ui.states

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.nullpointer.noursecompose.R
import timber.log.Timber

class ConfigScreenState(
    context: Context,
    scaffoldState: ScaffoldState
) : SimpleScreenState(scaffoldState, context) {

    private lateinit var mediaPlayer: MediaPlayer
    var isPlaying by mutableStateOf(false)


    private fun getUriMediaPlayer(indexSound: Int = -1): Uri {
        return if (indexSound != -1) {
            val sound = when (indexSound) {
                0 -> R.raw.sound1
                1 -> R.raw.sound2
                2 -> R.raw.sound3
                3 -> R.raw.sound4
                else -> R.raw.sound5
            }
            Uri.parse("android.resource://" + context.packageName.toString() + "/" + sound)
        } else {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        }
    }

    fun togglePlayPause() {
        isPlaying = if (isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
            false
        } else {
            mediaPlayer.start()
            true
        }
    }

    fun setNewSong(indexSound: Int = -1) {
        if (this::mediaPlayer.isInitialized) releaseMediaPlayer()
        mediaPlayer = MediaPlayer.create(context, getUriMediaPlayer(indexSound)).also {
            it.setOnCompletionListener { isPlaying = false }
        }

    }

    fun releaseMediaPlayer() {
        if (isPlaying) {
            try {
                mediaPlayer.reset()
            } catch (e: Exception) {
                Timber.d("Error stop mediaPlayer $e")
            } finally {
                isPlaying = false
            }
        }
        mediaPlayer.release()
    }
}

@Composable
fun rememberConfigScreenState(
    context: Context = LocalContext.current,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) = remember(scaffoldState) {
    ConfigScreenState(context, scaffoldState)
}