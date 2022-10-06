package com.nullpointer.noursecompose.ui.screen.logs.componets.items

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.toFormat
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.registry.Log

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemLog(
    log: Log,
    isSelectEnable: Boolean,
    actionClick: (Log) -> Unit,
    changeItemSelected: (ItemSelected) -> Unit,
    context: Context = LocalContext.current
) {

    val backgroundSelect by animateColorAsState(
        if (log.isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface
    )

    val timeText = remember {
        context.getString(R.string.text_hour_registry, log.timestamp.toFormat(context))
    }

    Surface(
        color = backgroundSelect,
        shape = MaterialTheme.shapes.small,
        elevation = dimensionResource(id = R.dimen.elevation_surface),
        modifier = Modifier
            .combinedClickable(
                onClick = { if (isSelectEnable) changeItemSelected(log) else actionClick(log) },
                onLongClick = { if (!isSelectEnable) changeItemSelected(log) }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.height_row_log))
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                stringResource(id = log.type.title),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.error,
                fontWeight = FontWeight.W600
            )
            Text(
                text = stringResource(id = log.type.description, log.idAlarm),
                style = MaterialTheme.typography.body1
            )
            Text(
                text = timeText,
                style = MaterialTheme.typography.caption
            )
        }
    }
}