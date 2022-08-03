package com.nullpointer.noursecompose.ui.screen.logs

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    registry: Log,
    actionClick: (Log) -> Unit,
    changeItemSelected: (ItemSelected) -> Unit,
    isSelectEnable: Boolean,
    context: Context = LocalContext.current
) {

    val color by remember(registry.isSelected) {
        derivedStateOf { if (registry.isSelected) Color.Cyan.copy(alpha = 0.5f) else Color.Unspecified }
    }

    val timeText by remember {
        derivedStateOf {
            context.getString(R.string.text_hour_registry, registry.timestamp.toFormat(context))
        }
    }

    Card(shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(4.dp)
            .combinedClickable(
                onClick = { if (isSelectEnable) changeItemSelected(registry) else actionClick(registry) },
                onLongClick = { if (!isSelectEnable) changeItemSelected(registry) }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind { drawRect(color) }
                .padding(10.dp)
        ) {
            Text(
                stringResource(id = registry.type.title),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.error,
                fontWeight = FontWeight.W600
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = registry.type.description, registry.idAlarm),
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = timeText,
                style = MaterialTheme.typography.caption
            )
        }
    }
}