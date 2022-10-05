package com.nullpointer.noursecompose.ui.screen.config

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.notify.TypeNotify

@Composable
fun RadioButtonNotifyType(
    currentNotify: TypeNotify,
    changeNotify: (TypeNotify) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        TitleConfig(textTitle = stringResource(R.string.mini_title_type_alarm))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {


            TypeNotify.values().forEach {
                Row(modifier = Modifier
                    .clickable { changeNotify(it) }
                    .padding(horizontal = 15.dp)) {
                    RadioButton(
                        selected = it == currentNotify,
                        onClick = null,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(stringResource(id = it.title))
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            stringResource(id = it.description),
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }
        }
    }
}