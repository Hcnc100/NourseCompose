package com.nullpointer.noursecompose.ui.screen.logs.componets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.core.utils.toFormat
import com.nullpointer.noursecompose.models.registry.Registry

@Composable
fun ItemLog(
    registry: Registry,
) {
    val context = LocalContext.current
    Card(shape = RoundedCornerShape(10.dp), modifier = Modifier.padding(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
            Text(text = registry.type.name,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.error, modifier = Modifier.weight(1f))
            Column(modifier = Modifier.weight(3f)) {
                Text("ID ${registry.idAlarm}")
                Text("Hora del registro ${registry.timestamp.toFormat(context)}")
            }
        }
    }
}