package com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen.viewModel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Insert
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.delegates.SavableComposeState
import com.nullpointer.noursecompose.models.alarm.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.shouheng.compress.Compress
import me.shouheng.compress.concrete
import me.shouheng.compress.strategy.config.ScaleMode
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NameAndImgViewModel @Inject constructor(
    state: SavedStateHandle,
) : ViewModel() {
    companion object {
        private const val KEY_NAME = "KEY_NAME"
        private const val KEY_FILE = "KEY_FILE"
        private const val KEY_ERROR_NAME = "KEY_ERROR_NAME"
        private const val KEY_VM_NAME_INIT = "KEY_VM_NAME_INIT"
        const val MAX_LENGTH_NAME = 50
    }

    var nameAlarm: String by SavableComposeState(state, KEY_NAME, "")
        private set

    var fileImg: File? by SavableComposeState(state, KEY_FILE, null)
        private set

    var errorName: Int by SavableComposeState(state, KEY_ERROR_NAME, -1)
        private set

    private var isInit: Boolean by SavableComposeState(state, KEY_VM_NAME_INIT, false)

    val hasErrorName get() = errorName != -1
    val counterName get() = "${nameAlarm.length} / $MAX_LENGTH_NAME"

    private var jobCompress: Job? = null
    var isCompress = mutableStateOf(false)
        private set

    fun changeInit(name:String){
        if(!isInit){
            nameAlarm=name
            isInit=false
        }
    }


    fun changeName(newName: String) {
        nameAlarm = newName
        validateName()
    }

    fun validateName() {
        errorName = when {
            nameAlarm.isEmpty() -> R.string.error_empty_name
            nameAlarm.length > MAX_LENGTH_NAME -> R.string.error_name_long
            else -> -1
        }
    }

    fun changeFileImg(uri: Uri, context: Context) {
        jobCompress?.cancel()
        jobCompress = viewModelScope.launch {
            Timber.d("Init compress")
            isCompress.value = true
            val bitmapCompress = Compress.with(context, uri).setQuality(80).concrete {
                withMaxHeight(500f)
                withMaxWidth(500f)
                withScaleMode(ScaleMode.SCALE_SMALLER)
                withIgnoreIfSmaller(true)
            }.get(Dispatchers.IO)
            fileImg = bitmapCompress
            Timber.d("finish compress")
            isCompress.value = false
        }
    }


}