package com.nullpointer.noursecompose.ui.dialogs

import android.text.format.DateUtils
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.TimeUtils.getHourAndMinutesFromMillis

@Composable
fun DialogSelectHourRepeat(
    changeTimeRepeater: (newValue: Long) -> Unit,
    valueDefects: Long,
    hideDialog: () -> Unit,
) {

    val (hoursFromMillis,minutesFromMillis)=getHourAndMinutesFromMillis(valueDefects)

    val (hourTime, changeHourTime) = rememberSaveable { mutableStateOf(hoursFromMillis) }
    val (minuteTime, changeMinuteTime) = rememberSaveable { mutableStateOf(minutesFromMillis) }

    AlertDialog(onDismissRequest = { hideDialog() },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(R.string.text_repeater_every),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = stringResource(R.string.text_hours),
                            style = MaterialTheme.typography.subtitle2)
                        NumberPicker(max = 100, min = 0, defaultValue = hourTime, changeHourTime)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = stringResource(R.string.text_minutes),
                            style = MaterialTheme.typography.subtitle2)
                        NumberPicker(max = 59, min = 0, defaultValue = minuteTime, changeMinuteTime)
                    }
                }

            }
        },

        confirmButton = {
            TextButton(
                onClick = {
                    changeTimeRepeater(hourTime * DateUtils.HOUR_IN_MILLIS + minuteTime * DateUtils.MINUTE_IN_MILLIS)
                    hideDialog()
                }
            ) { Text(stringResource(R.string.text_accept)) }
        },
        dismissButton = {
            TextButton(onClick = hideDialog) {
                Text(stringResource(R.string.text_cancel)) }
        }
    )


}

@Composable
fun NumberPicker(max: Int, min: Int, defaultValue: Int, onChangeValue: (newValue: Int) -> Unit) {
    val colorSeparator = MaterialTheme.colors.primary.toArgb()
    val colorText = MaterialTheme.colors.onBackground.toArgb()
    val psValue = with(LocalDensity.current) { 22.sp.toPx() }
    AndroidView(factory = { context ->
        MaterialNumberPicker(context).apply {
            editable = true
            maxValue = max
            minValue = min
            separatorColor = colorSeparator
            textColor = colorText
            value = defaultValue
            wrapSelectorWheel = false
            textSize = psValue.toInt()
            setOnValueChangedListener { _, _, newVal ->
                onChangeValue(newVal)
            }
        }
    })
}