package com.nullpointer.noursecompose.ui.screen.addAlarm.viewModel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.delegates.PropertySavableAlarmTime
import com.nullpointer.noursecompose.core.delegates.PropertySavableImg
import com.nullpointer.noursecompose.core.delegates.PropertySavableString
import com.nullpointer.noursecompose.core.delegates.SavableComposeState
import com.nullpointer.noursecompose.domain.compress.CompressRepository
import com.nullpointer.noursecompose.models.alarm.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class AddAlarmViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val compressRepository: CompressRepository
) : ViewModel() {

    companion object {
        private const val MAX_LENGTH_NAME = 70
        private const val MAX_LENGTH_DESCRIPTION = 250
        private const val TAG_IMG_ALARM = "TAG_IMG_ALARM"
        private const val TAG_NAME_ALARM = "TAG_NAME_ALARM"
        private const val KEY_DIALOG_REPEAT = "KEY_DIALOG_REPEAT"
        private const val TAG_DESCRIPTION_ALARM = "TAG_DESCRIPTION_ALARM"
    }

    private val _messageAddAlarm = Channel<Int>()
    val messageAddAlarm = _messageAddAlarm.receiveAsFlow()


    val nameAlarm = PropertySavableString(
        savedState = stateHandle,
        label = R.string.label_name_alarm,
        hint = R.string.hint_name_alarm,
        maxLength = MAX_LENGTH_NAME,
        emptyError = R.string.error_empty_name,
        lengthError = R.string.error_name_long,
        tagSavable = TAG_NAME_ALARM
    )

    val description = PropertySavableString(
        savedState = stateHandle,
        label = R.string.label_description_alarm,
        hint = R.string.hint_description_alarm,
        maxLength = MAX_LENGTH_DESCRIPTION,
        lengthError = R.string.error_description_long,
        tagSavable = TAG_DESCRIPTION_ALARM
    )

    val imageAlarm = PropertySavableImg(
        state = stateHandle,
        scope = viewModelScope,
        tagSavable = TAG_IMG_ALARM,
        actionSendErrorCompress = { _messageAddAlarm.trySend(R.string.error_img_compress) },
        actionCompress = compressRepository::compressImage
    )

    val alarmTimeSavable = PropertySavableAlarmTime(state = stateHandle)

    var showDialogRepeat by SavableComposeState(stateHandle, KEY_DIALOG_REPEAT, false)
        private set

    fun changeVisibleDialogRepeat(isVisible: Boolean) {
        showDialogRepeat = isVisible
    }

    fun valueNameAlarm(callbackSuccess: () -> Unit) {
        nameAlarm.reValueField()
        if (!nameAlarm.hasError) {
            callbackSuccess()
        }
    }

    fun valueAlarmTime(callbackSuccess: (Alarm, Uri?) -> Unit) {
        if (!alarmTimeSavable.hasErrorRange) {
            val alarm = createAlarm()
            val imageAlarm = imageAlarm.getValueOrNull()
            callbackSuccess(alarm, imageAlarm)
        }
    }

    private fun createAlarm(): Alarm {
        return Alarm(
            name = nameAlarm.currentValue,
            description = description.currentValue,
            typeAlarm = alarmTimeSavable.typeAlarm,
            nextAlarm = alarmTimeSavable.timeNextAlarm,
            repeaterEvery = alarmTimeSavable.timeToRepeatAlarm,
            rangeInitAlarm = alarmTimeSavable.rangeAlarm.first,
            rangeFinishAlarm = alarmTimeSavable.rangeAlarm.second,
        )
    }

    fun clearAlarmFields() {
        nameAlarm.clearValue()
        description.clearValue()
        alarmTimeSavable.clearValue()
        imageAlarm.clearValue()
    }


}