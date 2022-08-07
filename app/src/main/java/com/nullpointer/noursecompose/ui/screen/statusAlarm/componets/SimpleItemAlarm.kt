package com.nullpointer.noursecompose.ui.screen.statusAlarm.componets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.ui.share.AlarmCurrent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SimpleItemAlarm(
    alarm: Alarm,
    clickAlarm:(Alarm)->Unit
) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        onClick = {clickAlarm(alarm)}
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            AlarmCurrent(
                imgAlarm = alarm.pathFile,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterHorizontally)
            )
            TitleAlarm(titleAlarm = alarm.name)
            DescriptionAlarm(descriptionAlarm = alarm.description)
        }
    }
}

@Composable
private fun DescriptionAlarm(
    descriptionAlarm: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = descriptionAlarm,
        style = MaterialTheme.typography.body1,
        fontSize = 14.sp,
        maxLines = 2
    )
}

@Composable
private fun TitleAlarm(
    titleAlarm: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = titleAlarm,
        style = MaterialTheme.typography.h5,
        fontSize = 16.sp, maxLines = 2
    )
}

