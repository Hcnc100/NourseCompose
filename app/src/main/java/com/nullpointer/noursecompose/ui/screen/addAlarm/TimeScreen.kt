package com.nullpointer.noursecompose.ui.screen.addAlarm

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.core.utils.TimeUtils.calculateRangeInDays
import com.nullpointer.noursecompose.models.alarm.AlarmTypes
import com.nullpointer.noursecompose.ui.screen.DialogDate

@Composable
fun TimeScreen() {
    val context = LocalContext.current
    val rangeAlarm = Pair(0L, 0L)
    ContentPage(title =  "Selecciona la hora de la alarma") {
        Column(verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxSize()) {
            Column {
                TextMiniTitle(textTitle = "Hora inicial")
                TextCenterValue("8:00") {}
            }

            AnimatedVisibility(
                visible = true,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    TextMiniTitle(textTitle = "Rango de dias")
                    FieldRangeAlarm(
                        textRange = calculateRangeInDays(
                            context = context,
                            time = rangeAlarm
                        ),
                        hasError = false,
                        currentSelect = Pair(0, 0),
                        titleSelect = "Rango de dias",
                        {}
                    )
                }

            }
        }

    }

}

@Composable
fun TextCenterValue(textValue: String, actionClick: () -> Unit) {
    Text(
        textValue,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { actionClick() },
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h5
    )
}

@Composable
fun TextMiniTitle(
    textTitle: String,
) {
    Text(
        textTitle,
        modifier = Modifier.padding(vertical = 10.dp),
        style = MaterialTheme.typography.caption,
        fontWeight = FontWeight.Bold
    )
}


@Composable
fun FieldRangeAlarm(
    textRange: String,
    hasError: Boolean,
    currentSelect: Pair<Long, Long>,
    titleSelect: String,
    changeTextRange: (rangeAlarm: Pair<Long, Long>) -> Unit,
) {
    val activity = LocalContext.current as AppCompatActivity
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxWidth(.9f),
            horizontalAlignment = Alignment.End) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        DialogDate.showDatePickerRange(activity = activity,
                            updatedDate = changeTextRange,
                            currentSelect = currentSelect)
                    },
                label = { Text(titleSelect) },
                value = textRange,
                onValueChange = {},
                enabled = false,
            )
            if (hasError)
                Text(text = "Debe seleccionar minimo el rango de 1 dia",
                    style = MaterialTheme.typography.caption,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colors.error
                )
        }
    }
}
