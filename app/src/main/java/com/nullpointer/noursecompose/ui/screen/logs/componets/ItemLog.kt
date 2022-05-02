package com.nullpointer.noursecompose.ui.screen.logs.componets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.toFormat
import com.nullpointer.noursecompose.models.registry.Registry

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemLog(
    registry: Registry,
    actionClick: (Long) -> Unit,
) {
    val context = LocalContext.current
    Card(shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(4.dp),
        onClick = { actionClick(registry.idAlarm) }) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
            Text(stringResource(id = registry.type.title),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.error,
                fontWeight = FontWeight.W600)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(id = registry.type.description, registry.idAlarm),
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(stringResource(R.string.text_hour_registry, registry.timestamp.toFormat(context)),
                style = MaterialTheme.typography.caption)
        }
    }
}