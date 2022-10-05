package com.nullpointer.noursecompose.data.local.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.getUriRaw
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SoundDataSourceImpl(
    private val context: Context
) : SoundDataSource {

    private var _isPlaying = MutableStateFlow(false)
    override var isPlaying: Flow<Boolean> = _isPlaying

    private val _isPlayInLoop = MutableStateFlow(false)
    override val isPlayInLoop: Flow<Boolean> = _isPlayInLoop


    private val mediaPlayer: MediaPlayer = MediaPlayer().apply {
        setOnCompletionListener {
            if (_isPlayInLoop.value) {
                it.seekTo(0)
                it.start()
            } else {
                _isPlaying.value = false
            }
        }

        setAudioAttributes(
            AudioAttributes.Builder()
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .setLegacyStreamType(AudioManager.STREAM_ALARM)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
    }

    override val listSoundRaw = listOf(
        R.raw.sound1,
        R.raw.sound2,
        R.raw.sound3,
        R.raw.sound4,
        R.raw.sound5
    )

    private fun getUriMediaPlayer(indexSound: Int): Uri? {
        return when (indexSound) {
            -1 -> RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            in listSoundRaw.indices -> context.getUriRaw(listSoundRaw[indexSound])
            else -> throw RuntimeException("The index is invalid")
        }
    }

    override fun togglePlayPause() {
        _isPlaying.value = if (_isPlaying.value) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
            false
        } else {
            mediaPlayer.start()
            true
        }
    }

    override fun changeSong(indexSound: Int) {
        _isPlaying.value = false
        getUriMediaPlayer(indexSound)?.let {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(context, it)
            mediaPlayer.prepare()
        }
    }

    override fun releaseSound() {
        mediaPlayer.release()
    }

    override fun stopSoundInLoop() {
        _isPlaying.value = false
        _isPlayInLoop.value = false
        mediaPlayer.stop()
        mediaPlayer.reset()
    }


    override fun startSoundInLoop() {
        _isPlayInLoop.value = true
        _isPlaying.value = true
        mediaPlayer.reset()
        mediaPlayer.start()
    }


}