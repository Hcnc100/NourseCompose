package com.nullpointer.noursecompose.ui.screen.alarms.componets.items

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    modifier: Modifier = Modifier,
    changeSelectState: (ItemSelected) -> Unit,
    actionClickSimple: (alarm: Alarm) -> Unit
) {

    val colorSelected by animateColorAsState(
        targetValue = if (alarm.isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface
    )

    Surface(
        modifier = modifier,
        color = colorSelected,
        shape = MaterialTheme.shapes.small,
        elevation = dimensionResource(id = R.dimen.elevation_surface)
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .combinedClickable(
                    onClick = {
                        if (isSelectedEnable) changeSelectState(alarm) else actionClickSimple(
                            alarm
                        )
                    },
                    onLongClick = { if (!isSelectedEnable) changeSelectState(alarm) }
                )
        ) {
            ImageAlarm(
                path = alarm.pathFile,
                imageDefault = R.drawable.ic_alarm,
                modifier = Modifier
                    .weight(2F)
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier
                    .weight(3F)
                    .padding(10.dp)
            ) {
                TextStateAlarm(isActive = alarm.isActive)
                TextInfoAlarm(
                    titleAlarm = alarm.name,
                    timeNextAlarm = alarm.nextAlarm
                )
            }
        }
    }
}

@Composable
private fun TextInfoAlarm(
    titleAlarm: String,
    timeNextAlarm: Long?,
    context: Context = LocalContext.current
) {
    val textNextAlarm = remember(timeNextAlarm) {
        if (timeNextAlarm != null) getStringTimeAboutNow(timeNextAlarm, context) else null
    }
    Text(
        titleAlarm,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(vertical = 3.dp)
    )
    textNextAlarm?.let {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                stringResource(R.string.sub_title_next_alarm),
                fontWeight = FontWeight.W300,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = it,
                fontWeight = FontWeight.W300,
                style = MaterialTheme.typography.caption,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
private fun TextStateAlarm(
    isActive: Boolean,
) {

    val (textState, colorTextState) = remember(isActive) {
        if (isActive)
            Pair(R.string.text_state_active, Color.Green)
        else
            Pair(R.string.text_state_inactive, Color.Red)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            stringResource(R.string.sub_title_state_alarm),
            style = MaterialTheme.typography.subtitle2.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.W300
            )
        )
        Text(
            text = stringResource(id = textState),
            style = MaterialTheme.typography.subtitle2.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = colorTextState
            )
        )
    }
}