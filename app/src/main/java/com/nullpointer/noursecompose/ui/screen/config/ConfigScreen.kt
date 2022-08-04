package com.nullpointer.noursecompose.ui.screen.config

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.ui.interfaces.ActionRootDestinations
import com.nullpointer.noursecompose.ui.navigation.MainNavGraph
import com.nullpointer.noursecompose.ui.screen.config.viewModel.ConfigViewModel
import com.nullpointer.noursecompose.ui.share.mpGraph.ToolbarBack
import com.nullpointer.noursecompose.ui.states.ConfigScreenState
import com.nullpointer.noursecompose.ui.states.rememberConfigScreenState
import com.ramcosta.composedestinations.annotation.Destination


@MainNavGraph
@Composable
@Destination
fun ConfigScreen(
    actionRootDestinations: ActionRootDestinations,
    configViewModel: ConfigViewModel = hiltViewModel(),
    configScreenState: ConfigScreenState = rememberConfigScreenState()
) {
    val typeNotify by configViewModel.typeNotify.collectAsState()
    val indexSound by configViewModel.intSound.collectAsState()

    DisposableEffect(key1 = indexSound) {
        configScreenState.setNewSong(indexSound)
        onDispose { configScreenState.releaseMediaPlayer() }
    }

    Scaffold(
        topBar = {
            ToolbarBack(
                title = stringResource(R.string.title_config_screen),
                actionBack = actionRootDestinations::backDestination
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            RadioButtonNotifyType(
                currentNotify = typeNotify,
                changeNotify = configViewModel::changeTypeNotify
            )
            Spacer(modifier = Modifier.height(20.dp))
            SelectSoundAlarm(
                indexSound = indexSound,
                changeIndexSound = configViewModel::changeIntSound
            )
            Spacer(modifier = Modifier.height(5.dp))
            ButtonPlayOrPause(
                togglePlayPause = configScreenState::togglePlayPause,
                isPlaying = configScreenState.isPlaying
            )
        }
    }
}
@Composable
fun TitleConfig(
    textTitle: String
) {
    Text(
        text = textTitle,
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(10.dp),
        fontSize = 14.sp
    )
}