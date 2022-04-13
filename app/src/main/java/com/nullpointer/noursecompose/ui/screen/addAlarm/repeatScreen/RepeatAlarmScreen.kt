package com.nullpointer.noursecompose.ui.screen.addAlarm.repeatScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.alarm.AlarmTypes
import com.nullpointer.noursecompose.ui.screen.addAlarm.ContentPage
import com.nullpointer.noursecompose.ui.screen.addAlarm.timeScreen.viewModel.TimeViewModel

@Composable
fun RepeatAlarmScreen(
    repeatViewModel: TimeViewModel,
) {
    val options = listOf(
        AlarmTypes.INDEFINITELY,
        AlarmTypes.RANGE,
        AlarmTypes.ONE_SHOT,
    )
    ContentPage(title = stringResource(R.string.title_type_alarm)) {
        Column(modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly) {
            options.forEach {
                Row(modifier = Modifier
                    .clickable { repeatViewModel.changeType(it) }
                    .padding(vertical = 5.dp)) {
                    RadioButton(
                        selected = it == repeatViewModel.typeAlarm,
                        onClick = null,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(it.stringResource)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(it.stringDescription, style = MaterialTheme.typography.caption)
                    }
                }
            }
        }
    }
}



