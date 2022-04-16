package com.nullpointer.noursecompose.ui.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nullpointer.noursecompose.R

@Composable
fun DialogDetails(
    nameMeasure: String,
    measureFullSuffix: String,
    actionHiddenDialog: () -> Unit,
    actionEdit: (value: Float) -> Unit,
) {
    val (textInputMeasure, changeValueMeasure) = rememberSaveable { mutableStateOf("") }
    val (hasError, changeHasError) = rememberSaveable { mutableStateOf(false) }
    val actionAccept = {
        val valueMeasure = textInputMeasure.toFloatOrNull()
        if (valueMeasure != null) {
            actionEdit(valueMeasure)
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