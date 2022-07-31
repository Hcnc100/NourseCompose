package com.nullpointer.noursecompose.core.delegates

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.*
import me.shouheng.compress.Compress
import me.shouheng.compress.concrete
import me.shouheng.compress.strategy.config.ScaleMode
import timber.log.Timber

class PropertySavableImg(
    state: SavedStateHandle,
    private val scope: CoroutineScope,
    private val actionSendError: () -> Unit
) {

    companion object {
        private const val INIT_ID = 1
        private const val FIN_ID = 1234567890
    }

    private val idSaved = "SAVED_PROPERTY_IMG_${(INIT_ID..FIN_ID).random()}"

    private var initValue:String? by SavableProperty(state, "$idSaved-initValue", null)

    var currentPath: String? by SavableComposeState(state, "$idSaved-value",null)
        private set

    private var jobCompress: Job? = null

    var isCompress by mutableStateOf(false)
        private set

    val isEmpty get() = currentPath == null

    val hasChanged get() = currentPath != initValue

    fun initValue(value: String) {
        currentPath = value
        initValue = value
    }

    fun changeValue(newValue: Uri, context: Context) {
        jobCompress?.cancel()
        jobCompress = scope.launch {
            try {
                isCompress = true
                val bitmapCompress = Compress.with(context, newValue)
                    .setQuality(70)
                    .concrete {
                        withMaxHeight(500f)
                        withMaxWidth(500f)
                        withScaleMode(ScaleMode.SCALE_SMALLER)
                        withIgnoreIfSmaller(true)
                    }.get(Dispatchers.IO)
                currentPath = bitmapCompress.absolutePath
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> throw e
                    else -> {
                        Timber.e("Job compress exception $e")
                        currentPath = initValue
                        actionSendError()
                    }
                }
            } finally {
                isCompress = false
            }
        }
    }

    fun clearValue() {
        isCompress = false
        currentPath = null
        initValue = null
        jobCompress?.cancel()
    }
}