package com.nullpointer.noursecompose.ui.screen.config

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R

@Composable
 fun ButtonPlayOrPause(
    isPlaying: Boolean,
    togglePlayPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (icon, description) = remember(isPlaying) {
        if (isPlaying)
            Pair(R.drawable.ic_stop, R.string.text_stop_sound)
        else
            Pair(R.drawable.ic_play, R.string.text_play_sound)
    }

    TextButton(
        modifier = modifier.width(150.dp),
        onClick = togglePlayPause
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = stringResource(description)
            )
            Text(stringResource(R.string.title_sound_preview))
        }
    }
}