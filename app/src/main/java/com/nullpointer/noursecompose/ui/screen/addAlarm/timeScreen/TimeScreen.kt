package com.nullpointer.noursecompose.ui.screen.addAlarm.timeScreen

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.nullpointer.noursecompose.ui.dialogs.DialogSelectHour
import com.nullpointer.noursecompose.ui.dialogs.DialogDate
import com.nullpointer.noursecompose.ui.dialogs.DialogDate.Companion.showTimePicker
import com.nullpointer.noursecompose.ui.screen.addAlarm.ContentPage
import com.nullpointer.noursecompose.ui.screen.addAlarm.timeScreen.viewModel.TimeViewModel

@Composable
fun TimeScreen(
    timeViewModel: TimeViewModel,
) {
    val context = LocalContext.current
    val (isShowRepeatDialog, changeIsShowRepeatTimeDialog) = rememberSaveable {
        mutableStateOf(false)
    }

    val timePicker = remember {
        showTimePicker(activity = context,
            updatedDate = timeViewModel::changeTimeInitAlarm)
    }

    ContentPage(title = stringResource(R.string.title_select_init_alarm)) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp)) {
            Column {
                TextMiniTitle(textTitle = stringResource(R.string.title_init_time))
                TextCenterValue(
                    textValue = timeViewModel.timeInitAlarm.toFormatOnlyTime(context),
                    actionClick = {
                        if (timePicker.isAdded) {
                            timePicker.dismiss()
                        } else {
                            timePicker.show((context as AppCompatActivity).supportFragmentManager,
                                timePicker.toString())
                        }
                    })
            }



            if (timeViewModel.typeAlarm != AlarmTypes.ONE_SHOT) {
                Spacer(modifier = Modifier.height(30.dp))
                Column {
                    TextMiniTitle(textTitle = stringResource(R.string.title_repeat_every))
                    TextCenterValue(
                        textValue = TimeUtils.timeRepeatInMillisToString(
                            timeViewModel.timeToRepeatAlarm,
                            context,
                            false),
                        actionClick = { changeIsShowRepeatTimeDialog(true) }
                    )
                }
            }


            if (timeViewModel.typeAlarm == AlarmTypes.RANGE) {
                Spacer(modifier = Modifier.height(30.dp))
                Column {
                    TextMiniTitle(textTitle = stringResource(R.string.title_range_days))
                    FieldRangeAlarm(
                        hasError = timeViewModel.hasErrorRange,
                        currentSelect = timeViewModel.rangeAlarm,
                        errorString = timeViewModel.errorRange,
                        titleSelect = stringResource(R.string.title_range_days),
                        changeTextRange = timeViewModel::changeRangeAlarm,
                        textRange = calculateRangeInDays(
                            context = context,
                            time = timeViewModel.rangeAlarm)

                    )
                }
            }

            if (!timeViewModel.hasErrorRange) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.padding(vertical = 10.dp)) {
                    Text(text = stringResource(R.string.title_next_alarm),
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.W200)
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(text = TimeUtils.getStringTimeAboutNow(timeViewModel.timeNextAlarm,
                        context),
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.W300)
                }
            }

            if (isShowRepeatDialog)
                DialogSelectHour(
                    valueDefects = timeViewModel.timeToRepeatAlarm,
                    changeTimeRepeater = timeViewModel::changeTimeToRepeatAlarm,
                    hideDialog = { changeIsShowRepeatTimeDialog(false) })

        }
    }

}

@Composable
fun TextCenterValue(textValue: String, actionClick: () -> Unit) {
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
fun TextMiniTitle(
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
fun FieldRangeAlarm(
    textRange: String,
    hasError: Boolean,
    currentSelect: Pair<Long, Long>,
    titleSelect: String,
    errorString: Int,
    changeTextRange: (rangeAlarm: Pair<Long, Long>) -> Unit,
) {
    val activity = LocalContext.current as AppCompatActivity
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxWidth(.9f),
            horizontalAlignment = Alignment.End) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        DialogDate.showDatePickerRange(activity = activity,
                            updatedDate = changeTextRange,
                            currentSelect = currentSelect)
                    },
                label = { Text(titleSelect) },
                value = textRange,
                onValueChange = {},
                isError = hasError,
                enabled = false,
            )
            if (hasError)
                Text(text = stringResource(id = errorString),
                    style = MaterialTheme.typography.caption,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colors.error
                )
        }
    }
}
