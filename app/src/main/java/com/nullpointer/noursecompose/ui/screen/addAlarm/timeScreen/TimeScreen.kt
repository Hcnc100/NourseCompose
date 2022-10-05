package com.nullpointer.noursecompose.ui.screen.addAlarm.timeScreen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.delegates.PropertySavableAlarmTime
import com.nullpointer.noursecompose.core.utils.TimeUtils
import com.nullpointer.noursecompose.core.utils.TimeUtils.calculateRangeInDays
import com.nullpointer.noursecompose.core.utils.toFormatOnlyTime
import com.nullpointer.noursecompose.models.alarm.AlarmTypes
import com.nullpointer.noursecompose.ui.dialogs.DialogSelectHourRepeat
import com.nullpointer.noursecompose.ui.screen.addAlarm.TitleAddAlarm

@Composable
fun TimeScreen(
    showPickerTime: () -> Unit,
    showDateRangePicker: () -> Unit,
    isShowDialogRepeat: Boolean,
    modifier: Modifier = Modifier,
    alarmTime: PropertySavableAlarmTime,
    changeShowDialogRepeat: (Boolean) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TitleAddAlarm(title = stringResource(R.string.title_select_init_alarm))
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SelectTimeInit(
                currentTime = alarmTime.timeInitAlarm,
                showPickerTime = showPickerTime
            )
            if (alarmTime.typeAlarm != AlarmTypes.ONE_SHOT) {
                HoursToRepeat(
                    timeToRepeat = alarmTime.timeToRepeatAlarm,
                    showDialogRepeat = { changeShowDialogRepeat(true) })
            }
            if (alarmTime.typeAlarm == AlarmTypes.RANGE) {
                RangeAlarm(
                    rangeAlarm = alarmTime.rangeAlarm,
                    hasError = alarmTime.hasErrorRange,
                    errorRange = alarmTime.errorRange,
                    showDateRangePicker = showDateRangePicker
                )
            }
            TextNextAlarm(alarmTime.timeNextAlarm)
        }

    }
    if (isShowDialogRepeat)
        DialogSelectHourRepeat(
            valueDefects = alarmTime.timeToRepeatAlarm,
            changeTimeRepeater = alarmTime::changeTimeToRepeatAlarm,
            hideDialog = { changeShowDialogRepeat(false) })
}

@Composable
private fun TextNextAlarm(
    nextAlarm: Long,
    context: Context = LocalContext.current
) {

    val nextAlarmText = remember(nextAlarm) {
        TimeUtils.getStringTimeAboutNow(nextAlarm, context)
    }
    Row(
        modifier = Modifier.padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = stringResource(R.string.title_next_alarm),
            style = MaterialTheme.typography.caption.copy(
                fontWeight = FontWeight.W400,
                fontSize = 14.sp
            ),
        )
        Text(
            text = nextAlarmText,
            style = MaterialTheme.typography.caption.copy(
                fontWeight = FontWeight.W500,
                fontSize = 14.sp
            )
        )
    }
}

@Composable
private fun RangeAlarm(
    rangeAlarm: Pair<Long, Long>,
    hasError: Boolean,
    errorRange: Int,
    showDateRangePicker: () -> Unit
) {

    Column {
        TextMiniTitle(textTitle = stringResource(R.string.title_range_days))
        FieldRangeAlarm(
            rangeAlarm = rangeAlarm,
            hasError = hasError,
            errorRange = errorRange,
            showDateRangePicker = showDateRangePicker
        )
    }
}

@Composable
private fun HoursToRepeat(
    timeToRepeat: Long,
    context: Context = LocalContext.current,
    showDialogRepeat: () -> Unit
) {
    val textValue = remember(timeToRepeat) {
        TimeUtils.timeRepeatInMillisToString(
            context = context,
            includeSeconds = false,
            timeInMillis = timeToRepeat
        )
    }
    Column {
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
    showPickerTime: () -> Unit,
    context: Context = LocalContext.current
) {
    val hourValue = remember(currentTime) {
        currentTime.toFormatOnlyTime(context)
    }

    Column {
        TextMiniTitle(textTitle = stringResource(R.string.title_init_time))
        TextCenterValue(
            textValue = hourValue,
            actionClick = showPickerTime
        )
    }

}


@Composable
private fun TextCenterValue(textValue: String, actionClick: () -> Unit) {
    Text(
        text = textValue,
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
        text = textTitle,
        modifier = Modifier.padding(10.dp),
        style = MaterialTheme.typography.caption,
        fontWeight = FontWeight.Bold
    )
}


@Composable
private fun FieldRangeAlarm(
    rangeAlarm: Pair<Long, Long>,
    hasError: Boolean,
    errorRange: Int,
    activity: AppCompatActivity = LocalContext.current as AppCompatActivity,
    showDateRangePicker: () -> Unit
) {

    val textRange = remember(rangeAlarm) {
        calculateRangeInDays(activity, rangeAlarm)
    }
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(.9f),
            horizontalAlignment = Alignment.End
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDateRangePicker() },
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
