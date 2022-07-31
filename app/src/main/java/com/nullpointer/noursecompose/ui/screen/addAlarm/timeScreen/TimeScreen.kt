package com.nullpointer.noursecompose.ui.screen.addAlarm.timeScreen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.TimeUtils
import com.nullpointer.noursecompose.core.utils.TimeUtils.calculateRangeInDays
import com.nullpointer.noursecompose.core.utils.toFormatOnlyTime
import com.nullpointer.noursecompose.models.alarm.AlarmTypes
import com.nullpointer.noursecompose.ui.dialogs.DialogDate
import com.nullpointer.noursecompose.ui.dialogs.DialogDate.Companion.showTimePicker
import com.nullpointer.noursecompose.ui.dialogs.DialogSelectHourRepeat
import com.nullpointer.noursecompose.ui.screen.addAlarm.TitleAddAlarm
import com.nullpointer.noursecompose.ui.screen.addAlarm.viewModel.AddAlarmViewModel
import com.nullpointer.noursecompose.ui.states.AddAlarmScreenState

@Composable
fun TimeScreen(
    addAlarmViewModel: AddAlarmViewModel,
) {


    Column(modifier = Modifier.fillMaxSize()) {
        TitleAddAlarm(title = stringResource(R.string.title_select_init_alarm))
        SelectTimeInit(
            currentTime = addAlarmViewModel.alarmTime.timeInitAlarm,
            changeTimeInit = addAlarmViewModel.alarmTime::changeTimeInitAlarm
        )
        if (addAlarmViewModel.alarmTime.typeAlarm != AlarmTypes.ONE_SHOT) {
            HoursToRepeat(
                timeToRepeat = addAlarmViewModel.alarmTime.timeToRepeatAlarm,
                showDialogRepeat = { addAlarmViewModel.changeVisibleDialogRepeat(true) })
        }
        if (addAlarmViewModel.alarmTime.typeAlarm == AlarmTypes.RANGE) {
            RangeAlarm(
                rangeAlarm = addAlarmViewModel.alarmTime.rangeAlarm,
                changeRangeAlarm = addAlarmViewModel.alarmTime::changeRangeAlarm,
                hasError = addAlarmViewModel.alarmTime.hasErrorRange,
                errorRange = addAlarmViewModel.alarmTime.errorRange
            )
        }
        TextNextAlarm(addAlarmViewModel.alarmTime.timeNextAlarm)
    }
    if (addAlarmViewModel.showDialogRepeat)
        DialogSelectHourRepeat(
            valueDefects =addAlarmViewModel.alarmTime.timeToRepeatAlarm,
            changeTimeRepeater = addAlarmViewModel.alarmTime::changeTimeToRepeatAlarm,
            hideDialog = { addAlarmViewModel.changeVisibleDialogRepeat(false) })
}

@Composable
private fun TextNextAlarm(
    nextAlarm: Long,
    context: Context = LocalContext.current
) {

    val nextAlarmText by derivedStateOf {
        TimeUtils.getStringTimeAboutNow(nextAlarm, context)
    }

    Spacer(modifier = Modifier.height(20.dp))
    Row(modifier = Modifier.padding(vertical = 10.dp)) {
        Text(
            text = stringResource(R.string.title_next_alarm),
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.W200
        )
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = nextAlarmText,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.W300
        )
    }
}

@Composable
private fun RangeAlarm(
    rangeAlarm: Pair<Long, Long>,
    changeRangeAlarm: (Pair<Long, Long>) -> Unit,
    hasError: Boolean,
    errorRange: Int,
) {

    Column {
        Spacer(modifier = Modifier.height(30.dp))
        TextMiniTitle(textTitle = stringResource(R.string.title_range_days))
        FieldRangeAlarm(
            rangeAlarm = rangeAlarm,
            changeRangeAlarm = changeRangeAlarm,
            hasError = hasError,
            errorRange = errorRange
        )
    }
}

@Composable
private fun HoursToRepeat(
    timeToRepeat: Long,
    context: Context = LocalContext.current,
    showDialogRepeat: () -> Unit
) {
    val textValue by derivedStateOf {
        TimeUtils.timeRepeatInMillisToString(
            timeInMillis = timeToRepeat,
            context = context,
            includeSeconds = false
        )
    }
    Column {
        Spacer(modifier = Modifier.height(30.dp))
        TextMiniTitle(textTitle = stringResource(R.string.title_repeat_every))
        TextCenterValue(
            textValue = textValue,
            actionClick = showDialogRepeat
        )
    }
}


@Composable
private fun SelectTimeInit(
    currentTime: Long,
    changeTimeInit: (Long) -> Unit,
    context: Context = LocalContext.current,
) {
    val timePicker = remember {
        showTimePicker(activity = context, updatedDate = changeTimeInit)
    }
    val hourValue by derivedStateOf {
        currentTime.toFormatOnlyTime(context)
    }

    Column {
        TextMiniTitle(textTitle = stringResource(R.string.title_init_time))
        TextCenterValue(
            textValue = hourValue,
            actionClick = {
                if (timePicker.isAdded) {
                    timePicker.dismiss()
                } else {
                    timePicker.show(
                        (context as AppCompatActivity).supportFragmentManager,
                        timePicker.toString()
                    )
                }
            })
    }

}


@Composable
private fun TextCenterValue(textValue: String, actionClick: () -> Unit) {
    Text(
        textValue,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { actionClick() },
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h5
    )
}

@Composable
private fun TextMiniTitle(
    textTitle: String,
) {
    Text(
        textTitle,
        modifier = Modifier.padding(10.dp),
        style = MaterialTheme.typography.caption,
        fontWeight = FontWeight.Bold
    )
}


@Composable
private fun FieldRangeAlarm(
    rangeAlarm: Pair<Long, Long>,
    changeRangeAlarm: (Pair<Long, Long>) -> Unit,
    hasError: Boolean,
    errorRange: Int,
    activity: AppCompatActivity = LocalContext.current as AppCompatActivity
) {

    val textRange by derivedStateOf {
        calculateRangeInDays(activity, rangeAlarm)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(.9f),
            horizontalAlignment = Alignment.End
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        DialogDate.showDatePickerRange(
                            activity = activity,
                            updatedDate = changeRangeAlarm,
                            currentSelect = rangeAlarm
                        )
                    },
                label = { Text(stringResource(R.string.title_range_days)) },
                value = textRange,
                onValueChange = {},
                isError = hasError,
                enabled = false,
            )
            Text(
                text = if (hasError) stringResource(id = errorRange) else "",
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.End,
                color = MaterialTheme.colors.error
            )
        }
    }
}
