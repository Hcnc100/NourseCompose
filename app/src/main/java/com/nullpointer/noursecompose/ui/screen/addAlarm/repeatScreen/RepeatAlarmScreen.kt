package com.nullpointer.noursecompose.ui.screen.addAlarm.repeatScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
        modifier = modifier
            .padding(horizontal = 10.dp)
            .fillMaxSize(),
    ) {
        TitleAddAlarm(title = stringResource(R.string.title_type_alarm))
        AlarmTypes.values().forEach {
            ItemSelectType(
                alarmTypes = it,
                typeSelected = alarmTime.typeAlarm,
                changeType = alarmTime::changeType
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
    }
}


@Composable
private fun ItemSelectType(
    alarmTypes: AlarmTypes,
    typeSelected: AlarmTypes,
    changeType: (AlarmTypes) -> Unit
) {

    val isSelect by remember(typeSelected) {
        derivedStateOf { typeSelected == alarmTypes }
    }

    Row(modifier = Modifier
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


