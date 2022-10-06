package com.nullpointer.noursecompose.ui.screen.config

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.notify.TypeNotify
import com.nullpointer.noursecompose.ui.interfaces.ActionRootDestinations
import com.nullpointer.noursecompose.ui.navigation.MainNavGraph
import com.nullpointer.noursecompose.ui.screen.config.viewModel.SettingsViewModel
import com.nullpointer.noursecompose.ui.share.mpGraph.ToolbarBack
import com.nullpointer.noursecompose.ui.states.SimpleScreenState
import com.nullpointer.noursecompose.ui.states.rememberSimpleScreenState
import com.ramcosta.composedestinations.annotation.Destination


@MainNavGraph
@Composable
@Destination
fun SettingsScreen(
    actionRootDestinations: ActionRootDestinations,
    configViewModel: SettingsViewModel = hiltViewModel(),
    configScreenState: SimpleScreenState = rememberSimpleScreenState()
) {
    val typeNotify by configViewModel.typeNotify.collectAsState()
    val indexSound by configViewModel.intSound.collectAsState()
    val isPlaying by configViewModel.isPlaying.collectAsState()
    val isAlarmStart by configViewModel.isAlarmSound.collectAsState()


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
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            RadioButtonNotifyType(
                currentNotify = typeNotify,
                changeNotify = configViewModel::changeTypeNotify
            )

            AnimatedVisibility(visible = typeNotify == TypeNotify.ALARM && !isAlarmStart) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    SelectSoundAlarm(
                        indexSound = indexSound,
                        changeIndexSound = configViewModel::changeIntSound,
                        sizeListSound = configViewModel.listSoundSize,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ButtonPlayOrPause(
                        togglePlayPause = configViewModel::togglePlayPauseSound,
                        isPlaying = isPlaying,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
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