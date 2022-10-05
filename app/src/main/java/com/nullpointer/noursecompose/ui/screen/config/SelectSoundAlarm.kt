package com.nullpointer.noursecompose.ui.screen.config

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
    sizeListSound: Int,
    changeIndexSound: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val textSoundSelected =
        remember(indexSound) { if (indexSound == -1) R.string.text_sound_defect else R.string.text_sound_select }
    Column(
        modifier = modifier
    ) {
        TitleConfig(textTitle = stringResource(R.string.mini_title_sound_alarm))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.mini_title_sound))
            Box(modifier = Modifier.width(150.dp)) {
                OutlinedTextField(
                    value = stringResource(id = textSoundSelected, indexSound + 1),
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier.clickable { isExpanded = true },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_drop),
                            contentDescription = stringResource(R.string.description_drop_icon)
                        )
                    },
                )
                MenuDropSound(
                    sizeListSound = sizeListSound,
                    isExpanded = isExpanded,
                    hiddenDrop = { isExpanded = false },
                    changeSelectSound = {
                        changeIndexSound(it)
                        isExpanded = false
                    }
                )
            }
        }
    }

}

@Composable
private fun MenuDropSound(
    sizeListSound: Int,
    isExpanded: Boolean,
    hiddenDrop: () -> Unit,
    changeSelectSound: (Int) -> Unit
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = hiddenDrop,
    ) {
        (-1 until sizeListSound).forEach {
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
