package com.nullpointer.noursecompose.ui.screen.addAlarm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.models.alarm.AlarmTypes

@Composable
fun RepeatAlarmScreen() {
    val (typeAlarm, changeTypeAlarm) = rememberSaveable { mutableStateOf(AlarmTypes.ONE_SHOT) }
    ContentPage(title = "Como quieres que la alarma se repita") {
        OptionsRepeater(typeAlarm, changeTypeAlarm)
    }

}

@Composable
fun OptionsRepeater(
    alarmTypeSelect: AlarmTypes,
    changeAlarmTypes: (AlarmTypes) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = listOf(
        AlarmTypes.INDEFINITELY,
        AlarmTypes.RANGE,
        AlarmTypes.ONE_SHOT,
    )
    Column(modifier = modifier
        .padding(horizontal = 10.dp)
        .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly) {
        options.forEach {
            Row(modifier = Modifier
                .clickable { changeAlarmTypes(it) }
                .padding(vertical = 5.dp)) {
                RadioButton(
                    selected = it == alarmTypeSelect,
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


