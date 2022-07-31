package com.nullpointer.noursecompose.ui.screen.addAlarm.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.delegates.PropertySavableAlarmTime
import com.nullpointer.noursecompose.core.delegates.PropertySavableString
import com.nullpointer.noursecompose.core.delegates.SavableComposeState
import com.nullpointer.noursecompose.models.alarm.AlarmTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class AddAlarmViewModel @Inject constructor(
    stateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val MAX_LENGTH_NAME = 70
        private const val MAX_LENGTH_DESCRIPTION = 250
    }

    private val _messageAddAlarm = Channel<Int>()
    val messageAddAlarm = _messageAddAlarm.receiveAsFlow()

    val nameAlarm = PropertySavableString(
        state = stateHandle,
        label = R.string.label_name_alarm,
        hint = R.string.hint_name_alarm,
        maxLength = MAX_LENGTH_NAME,
        emptyError = R.string.error_empty_name,
        lengthError = R.string.error_name_long
    )

    val description = PropertySavableString(
        state = stateHandle,
        label = R.string.label_description_alarm,
        hint = R.string.hint_description_alarm,
        maxLength = MAX_LENGTH_DESCRIPTION,
        lengthError = R.string.error_description_long
    )

    var imageAlarm: String? by SavableComposeState(stateHandle, "Pan", null)
        private set

    fun changeImg(newUri: String) {
        imageAlarm = newUri
    }

    val alarmTime = PropertySavableAlarmTime(
        state = stateHandle,
        defaultValue = AlarmTypes.ONE_SHOT,
        R.string.error_time_min_range
    )
    var showDialogRepeat by SavableComposeState(stateHandle, "KEY_DIALOG_REPEAT", false)
        private set

    fun changeVisibleDialogRepeat(isVisible: Boolean) {
        showDialogRepeat = isVisible
    }


}