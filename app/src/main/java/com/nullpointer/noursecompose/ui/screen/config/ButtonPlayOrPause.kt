package com.nullpointer.noursecompose.ui.screen.config

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R

@Composable
 fun ButtonPlayOrPause(
    togglePlayPause: () -> Unit,
    isPlaying: Boolean
) {
    val icon by remember(isPlaying) {
        derivedStateOf { if (isPlaying) R.drawable.ic_stop else R.drawable.ic_play }
    }

    Row {
        Box(modifier = Modifier.weight(.4f))
        TextButton(
            modifier = Modifier.weight(.6f),
            onClick = togglePlayPause
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = stringResource(R.string.description_button_preview)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.title_sound_preview))
            }
        }
    }
}