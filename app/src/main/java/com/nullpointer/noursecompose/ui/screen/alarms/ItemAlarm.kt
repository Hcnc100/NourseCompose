package com.nullpointer.noursecompose.ui.screen.alarms

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.TimeUtils.getStringTimeAboutNow
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.ui.share.ImageAlarm

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemAlarm(
    alarm: Alarm,
    isSelectedEnable: Boolean,
    changeSelectState: (ItemSelected) -> Unit,
    actionClickSimple: (alarm: Alarm) -> Unit,
) {

    val backgroundColor by derivedStateOf {
        if (alarm.isSelected) Color.Cyan.copy(alpha = 0.5f) else Color.Unspecified
    }

    Card(
        modifier = Modifier
            .padding(2.dp)
            .combinedClickable(
                onClick = { if (isSelectedEnable) changeSelectState(alarm) else actionClickSimple(alarm) },
                onLongClick = { if (!isSelectedEnable) changeSelectState(alarm) },
            ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .drawBehind { drawRect(backgroundColor) }
                .padding(10.dp)
        ) {
            Column(modifier = Modifier.weight(2f)) {
                TextStateAlarm(alarmIsActivate = alarm.isActive)
                TextInfoAlarm(
                    titleAlarm = alarm.name,
                    timeNextAlarm = alarm.nextAlarm
                )
            }
            alarm.pathFile?.let {
                ImageAlarm(
                    path = it,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
        }
    }
}

@Composable
private fun TextInfoAlarm(
    titleAlarm: String,
    timeNextAlarm: Long?,
    context: Context= LocalContext.current
) {
    val textNextAlarm by derivedStateOf {
        if (timeNextAlarm != null) getStringTimeAboutNow(timeNextAlarm, context) else null

    }
    Text(
        titleAlarm,
        modifier = Modifier.padding(vertical = 5.dp),
        style = MaterialTheme.typography.h6,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
    if (textNextAlarm != null) {
        Text(stringResource(R.string.sub_title_next_alarm),
            fontWeight = FontWeight.W300,
            modifier = Modifier.padding(vertical = 2.dp),
            style = MaterialTheme.typography.caption,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = titleAlarm,
            fontWeight = FontWeight.W300,
            modifier = Modifier.padding(vertical = 2.dp),
            style = MaterialTheme.typography.caption,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis)
    }
}


@Composable
private fun TextStateAlarm(
    alarmIsActivate: Boolean,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 5.dp)
    ) {
        Text(
            stringResource(R.string.sub_title_state_alarm),
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.W300
        )
        Text(
            if (alarmIsActivate) stringResource(R.string.text_state_active) else stringResource(R.string.text_state_inactive),
            color = if (alarmIsActivate) Color.Green else Color.Red,
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.W500
        )
    }
}