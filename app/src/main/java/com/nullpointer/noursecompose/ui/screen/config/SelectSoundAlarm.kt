package com.nullpointer.noursecompose.ui.screen.config

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R

@Composable
 fun SelectSoundAlarm(
    indexSound: Int,
    changeIndexSound: (Int) -> Unit,
) {
    val (expanded, changeExpanded) = rememberSaveable { mutableStateOf(false) }
    val textSoundSelected by remember(indexSound) {
        derivedStateOf {
            if (indexSound == -1) R.string.text_sound_defect else R.string.text_sound_select
        }
    }
    Column {
        TitleConfig(textTitle = stringResource(R.string.mini_title_sound_alarm))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Text(text = stringResource(R.string.mini_title_sound), modifier = Modifier.weight(.4f))
            Box(
                modifier = Modifier
                    .weight(.6f)
            ) {
                OutlinedTextField(
                    value = stringResource(id = textSoundSelected, indexSound + 1),
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier.clickable { changeExpanded(true) },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_drop),
                            contentDescription = stringResource(R.string.description_drop_icon)
                        )
                    },
                )
                MenuDropSound(
                    isExpanded = expanded,
                    hiddenDrop = { changeExpanded(false) },
                    changeSelectSound = {
                        changeIndexSound(it)
                        changeExpanded(false)
                    }
                )
            }
        }
    }

}

@Composable
private fun MenuDropSound(
    isExpanded: Boolean,
    hiddenDrop: () -> Unit,
    changeSelectSound: (Int) -> Unit
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = hiddenDrop,
    ) {
        (-1..4).forEach {
            val textSound by remember {
                derivedStateOf {
                    if (it == -1) R.string.text_sound_defect else R.string.text_sound_select
                }
            }
            DropdownMenuItem(
                onClick = { changeSelectSound(it) }
            ) {
                Text(
                    text = stringResource(id = textSound, it + 1)
                )
            }
        }
    }
}
