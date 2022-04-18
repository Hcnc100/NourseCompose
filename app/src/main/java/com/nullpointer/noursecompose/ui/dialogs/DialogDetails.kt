package com.nullpointer.noursecompose.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.toFormat
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.alarm.AlarmTypes

@Composable
fun DialogDetails(
    actionHiddenDialog: () -> Unit,
    alarm: Alarm,
) {
    AlertDialog(
        onDismissRequest = actionHiddenDialog,
        modifier = Modifier.fillMaxWidth(.98f),
        title = { Text("Detalles de la alarma") },
        text = {
            BodyDialogDetails(alarm = alarm)
        },
        confirmButton = {
            Button(onClick = {}) {
                Text(stringResource(R.string.text_accept))
            }
        },
        dismissButton = {
            TextButton(onClick = actionHiddenDialog) {
                Text(stringResource(R.string.text_cancel))
            }
        }
    )

}

@Composable
fun BodyDialogDetails(alarm: Alarm) {
    val context = LocalContext.current
    Column {
        Text(text = "Estado: ${if (alarm.isActive) "Activo" else "Inactivo"}")
        Text(text = "Nombre: ${alarm.title}")
        if(alarm.message.isNotEmpty()){
            Text(text = "Description: ${alarm.message}")
        }
        Text(text = "Tipo: ${alarm.typeAlarm.stringResource}")
        alarm.nextAlarm?.let {
            Text(text = "Proxima alarma:")
            Text(text = alarm.nextAlarm.toFormat(context, true))
        }
        if (alarm.typeAlarm == AlarmTypes.RANGE) {
            val timeInit = alarm.rangeInitAlarm!!.toFormat(context, true)
            val timeFinish = alarm.rangeFinishAlarm!!.toFormat(context, true)
            Text(text = "Rango:")
            Text(text = "Inicio: $timeInit")
            Text(text = "Fin: $timeFinish")
        }
        Text(text = "Fecha de creacion:")
        Text(text = alarm.createdAt.toFormat(context, true))
    }
}
