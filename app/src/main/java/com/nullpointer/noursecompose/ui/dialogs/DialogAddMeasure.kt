package com.nullpointer.noursecompose.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.delegates.PropertySavableString
import com.nullpointer.noursecompose.ui.share.EditableTextSavable

@Composable
fun DialogAddMeasure(
    measureFullSuffix: String,
    actionAdd: () -> Unit,
    actionHiddenDialog: () -> Unit,
    measureProperty: PropertySavableString
) {

    AlertDialog(
        onDismissRequest = actionHiddenDialog,
        modifier = Modifier.fillMaxWidth(.98f),
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.title_new_measure),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6,
                )
                EditableTextSavable(
                    maxLines = 1,
                    singleLine = true,
                    valueProperty = measureProperty,
                    trailingIcon = {
                        Text(
                            text = measureFullSuffix,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    },
                    keyboardActions = KeyboardActions(onDone = { actionAdd() }),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {
            Button(onClick = actionAdd) {
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
