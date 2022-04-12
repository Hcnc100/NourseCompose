package com.nullpointer.noursecompose.ui.screen.addAlarm

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.models.alarm.AlarmTypes

@Composable
fun RepeatAlarmScreen(
    actionNext: () -> Unit,
) {
    val (typeAlarm, changeTypeAlarm) = rememberSaveable { mutableStateOf(AlarmTypes.ONE_SHOT) }
    Column(modifier = Modifier
        .fillMaxSize()) {

        Text(text = "Como quieres que la alarma se repita",
            style = MaterialTheme.typography.h5, modifier = Modifier
                .padding(30.dp)
                .weight(1f))


        OptionsRepeater(typeAlarm, changeTypeAlarm, Modifier
            .weight(2f))


        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            Button(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.BottomEnd)) {
                Text("Siguiente")
            }
        }
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


