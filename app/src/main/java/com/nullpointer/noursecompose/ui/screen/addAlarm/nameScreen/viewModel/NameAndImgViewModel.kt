package com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen.viewModel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Insert
import com.nullpointer.noursecompose.core.delegates.SavableComposeState
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
         const val MAX_LENGTH_NAME=50
    }

    var nameAlarm: String by SavableComposeState(state, KEY_NAME, "")
    private set

    var fileImg: File? by SavableComposeState(state, KEY_FILE,null)
    private set

    var errorName:String by SavableComposeState(state, KEY_ERROR_NAME,"")
    private set

    private var jobCompress: Job? = null
    var isCompress = mutableStateOf(false)
        private set


    fun changeName(newName:String){
        nameAlarm=newName
        validateName()
    }

    fun validateName() {
        errorName=when{
            nameAlarm.isEmpty() -> "Se necesita un nombre para tu alarma"
            nameAlarm.length > MAX_LENGTH_NAME -> "El nombre no ppuyede ser tan largo"
            else ->""
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