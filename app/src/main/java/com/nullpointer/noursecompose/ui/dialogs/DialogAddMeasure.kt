package com.nullpointer.noursecompose.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R

@Composable
fun DialogAddMeasure(
    nameMeasure: String,
    measureFullSuffix: String,
    actionHiddenDialog: () -> Unit,
    actionAdd: (value: Float) -> Unit,
) {
    val (textInputMeasure, changeValueMeasure) = rememberSaveable { mutableStateOf("") }
    val (hasError, changeHasError) = rememberSaveable { mutableStateOf(false) }
    val actionAccept = {
        val valueMeasure = textInputMeasure.toFloatOrNull()
        if (valueMeasure != null) {
            actionAdd(valueMeasure)
            actionHiddenDialog()
        } else {
            changeHasError(true)
        }
    }

    AlertDialog(
        onDismissRequest = actionHiddenDialog,
        modifier = Modifier.fillMaxWidth(.98f),
        text = {
            BodyDialog(
                measureFullSuffix = measureFullSuffix,
                textInputMeasure = textInputMeasure,
                nameMeasure = nameMeasure,
                hasError = hasError,
                actionAccept = actionAccept,
                textMeasureChange = {
                    changeValueMeasure(it)
                    changeHasError(false)
                }
            )
        },
        confirmButton = {
            Button(onClick = actionAccept) {
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
fun BodyDialog(
    maxChars: Int = 5,
    measureFullSuffix: String,
    textInputMeasure: String,
    nameMeasure: String,
    hasError: Boolean,
    textMeasureChange: (String) -> Unit,
    actionAccept: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Text(
            text = stringResource(R.string.title_new_measure),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .width(160.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            OutlinedTextField(
                isError = hasError,
                value = textInputMeasure,
                onValueChange = { if (it.length <= maxChars) textMeasureChange(it) },
                label = { Text(nameMeasure) },
                trailingIcon = {
                    Text(measureFullSuffix,
                        modifier = Modifier.padding(horizontal = 8.dp))
                },
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = { actionAccept() }),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done),
            )
            Text(
                text = if (hasError) stringResource(R.string.message_error_measure) else "${textInputMeasure.length}/$maxChars",
                color = if (hasError) MaterialTheme.colors.error else Color.Unspecified,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(end = 5.dp)
            )
        }
    }
}
