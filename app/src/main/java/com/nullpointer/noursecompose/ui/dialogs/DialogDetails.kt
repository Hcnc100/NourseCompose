package com.nullpointer.noursecompose.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.toFormat
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.alarm.AlarmTypes

@Composable
fun DialogDetails(
    alarm: Alarm,
    deleterAlarm: () -> Unit,
    actionHiddenDialog: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = actionHiddenDialog,
        modifier = Modifier.fillMaxWidth(.98f),
        title = { Text(stringResource(R.string.title_alarm_details)) },
        text = { BodyDialogDetails(alarm = alarm) },
        buttons = {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                FloatingActionButton(
                    onClick = deleterAlarm,
                    backgroundColor = MaterialTheme.colors.error,
                    modifier = Modifier.size(45.dp)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                }
                OutlinedButton(onClick = actionHiddenDialog) {
                    Text(stringResource(R.string.text_accept))
                }
            }
        },
    )
}

@Composable
fun BodyDialogDetails(alarm: Alarm) {
    val context = LocalContext.current
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                stringResource(R.string.sub_title_state_alarm),
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.W300
            )
            Text(
                if (alarm.isActive) stringResource(R.string.text_state_active) else stringResource(R.string.text_state_inactive),
                color = if (alarm.isActive) Color.Green else Color.Red,
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.W500
            )
        }
        Text(text = stringResource(R.string.sub_title_name_alarm) + " ${alarm.name}")
        if (alarm.description.isNotEmpty()) {
            Text(text = stringResource(R.string.sub_title_description_alarm) + " ${alarm.description}")
        }
        Text(text = stringResource(R.string.sub_title_type_alarm) + " ${context.getString(alarm.typeAlarm.title)}")
        alarm.nextAlarm?.let {
            Text(text = stringResource(id = R.string.sub_title_next_alarm))
            Text(text = alarm.nextAlarm.toFormat(context, true))
        }
        if (alarm.typeAlarm == AlarmTypes.RANGE) {
            val timeInit = alarm.rangeInitAlarm?.toFormat(context, true)
            val timeFinish = alarm.rangeFinishAlarm?.toFormat(context, true)
            Text(text = stringResource(R.string.sub_title_range_alarm))
            Text(text = stringResource(R.string.sub_title_init_alarm) + " $timeInit")
            Text(text = stringResource(R.string.sub_title_finish_alarm) + " $timeFinish")
        }
        Text(text = stringResource(R.string.sub_title_date_alarm))
        Text(text = alarm.createdAt.toFormat(context, true))
        Spacer(modifier = Modifier.height(10.dp))
    }
}
