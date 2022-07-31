package com.nullpointer.noursecompose.ui.screen.statusAlarm.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.ImageUtils
import com.nullpointer.noursecompose.core.utils.toFormat
import com.nullpointer.noursecompose.models.alarm.Alarm

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SimpleItemAlarm(
    alarm: Alarm,
) {
    val context = LocalContext.current
    val image = remember { alarm.nameFile?.let { ImageUtils.loadImageFromStorage(it, context) } }
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Card(onClick = { isExpanded = !isExpanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp),
        shape = RoundedCornerShape(10.dp)) {
        Box {
            Column {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = alarm.title,
                        Modifier
                            .weight(2f)
                            .padding(horizontal = 20.dp),
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2)
                    if (alarm.nameFile != null) {
//                        ImageAlarm(contentDescription = stringResource(id = R.string.description_img_alarm),
//                            modifier = Modifier
//                                .weight(1f)
//                                .height(100.dp), bitmap = image)
                    }
                }
                if (isExpanded) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)) {
                        if (alarm.message.isNotEmpty()) {
                            RowInfo(description = stringResource(id = R.string.sub_title_description_alarm),
                                info = alarm.message)
                        }
                        RowInfo(description = stringResource(id = R.string.mini_title_type_alarm),
                            info = stringResource(id = alarm.typeAlarm.title))
                        RowInfo(description = stringResource(id = R.string.sub_title_next_alarm),
                            info = alarm.nextAlarm?.toFormat(context)
                                ?: stringResource(R.string.text_finish_alarm))
                    }
                }
            }
            Box(modifier = Modifier
                .padding(10.dp)
                .size(20.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
                .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center) {
                Icon(if (isExpanded) painterResource(id = R.drawable.ic_drop_up) else
                        painterResource(id = R.drawable.ic_arrow_drop),
                    contentDescription = stringResource(R.string.description_drop_up_icon))
            }
        }
    }
}

@Composable
fun RowInfo(description: String, info: String) {
    Row {
        Row {
            Text(description, style = MaterialTheme.typography.caption)
            Spacer(modifier = Modifier.width(10.dp))
            Text(info, style = MaterialTheme.typography.caption)
        }
    }
}