package com.nullpointer.noursecompose.ui.screen.addAlarm.repeatScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.delegates.PropertySavableAlarmTime
import com.nullpointer.noursecompose.models.alarm.AlarmTypes
import com.nullpointer.noursecompose.ui.screen.addAlarm.TitleAddAlarm

@Composable
fun RepeatAlarmScreen(
    modifier: Modifier = Modifier,
    alarmTime: PropertySavableAlarmTime
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(25.dp),
    ) {
        TitleAddAlarm(title = stringResource(R.string.title_type_alarm))
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AlarmTypes.values().forEach {
                ItemSelectType(
                    alarmTypes = it,
                    typeSelected = alarmTime.typeAlarm,
                    changeType = alarmTime::changeType
                )
            }
        }
    }
}


@Composable
private fun ItemSelectType(
    alarmTypes: AlarmTypes,
    typeSelected: AlarmTypes,
    changeType: (AlarmTypes) -> Unit
) {

    val isSelect = remember(typeSelected) { typeSelected == alarmTypes }

    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { changeType(alarmTypes) }
        .padding(vertical = 20.dp)) {
        RadioButton(
            selected = isSelect,
            onClick = null,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(stringResource(id = alarmTypes.title))
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                stringResource(id = alarmTypes.description),
                style = MaterialTheme.typography.caption
            )
        }
    }
}


