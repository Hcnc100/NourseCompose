package com.nullpointer.noursecompose.ui.screen.config

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.notify.TypeNotify
import com.nullpointer.noursecompose.services.SoundServices
import com.nullpointer.noursecompose.ui.screen.addAlarm.timeScreen.TextMiniTitle
import com.nullpointer.noursecompose.ui.screen.config.viewModel.ConfigViewModel
import com.nullpointer.noursecompose.ui.share.mpGraph.ToolbarBack
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


@Composable
@Destination
fun ConfigScreen(
    configViewModel: ConfigViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val typeNotify = configViewModel.typeNotify.collectAsState()
    val indexSound = configViewModel.intSound.collectAsState()
    val isSoundServices = SoundServices.alarmIsAlive
    val context = LocalContext.current
    val mediaPlayer = remember { MediaPlayer() }
    var isPlaying by remember { mutableStateOf(false) }
    var currentSound by remember { mutableStateOf(getUriMediaPlayer(-1, context)) }

    DisposableEffect(key1 = Unit) {
        onDispose {
            if (mediaPlayer.isPlaying) mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
    LaunchedEffect(key1 = indexSound.value) {
        currentSound = getUriMediaPlayer(indexSound.value, context)
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            isPlaying = false
        }
        mediaPlayer.reset()
    }

    // * stop sound if launch services
    LaunchedEffect(key1 = isSoundServices){
        if(isSoundServices){
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                isPlaying = false
            }
            mediaPlayer.reset()
        }
    }

    Scaffold(
        topBar = { ToolbarBack(title = "Configuracion", navigator::popBackStack) }
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            TextMiniTitle(textTitle = "Tipo de alarma")
            RadioButtonNotifyType(
                currentNotify = typeNotify.value,
                changeNotify = configViewModel::changeTypeNotify)
            Spacer(modifier = Modifier.height(20.dp))
            TextMiniTitle(textTitle = "Sonido de la alarma")
            SelectSoundAlarm(
                indexSound = indexSound.value,
                changeIndexSound = {
                    configViewModel.changeIntSound(it)
                })
            Spacer(modifier = Modifier.height(5.dp))
            Row {
                Box(modifier = Modifier.weight(.4f))
                TextButton(modifier = Modifier
                    .weight(.6f), onClick = {
                    if (mediaPlayer.isPlaying) {
                        isPlaying = false
                        mediaPlayer.pause()
                        mediaPlayer.seekTo(0)
                    } else {
                        mediaPlayer.setDataSource(context, currentSound)
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                        isPlaying = true
                    }
                }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (isPlaying) painterResource(id = R.drawable.ic_stop) else painterResource(
                            id = R.drawable.ic_play), contentDescription = "")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Preview")
                    }
                }
            }
        }
    }
}

fun getUriMediaPlayer(indexSound: Int, context: Context): Uri {
    return if (indexSound != -1) {
        val sound = when (indexSound) {
            0 -> R.raw.sound1
            1 -> R.raw.sound2
            2 -> R.raw.sound3
            3 -> R.raw.sound4
            else -> R.raw.sound5
        }
        Uri.parse("android.resource://" + context.packageName.toString() + "/" + sound)
    } else {
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    }
}

@Composable
fun SelectSoundAlarm(
    indexSound: Int,
    changeIndexSound: (Int) -> Unit,
) {
    val (expanded, changeExpanded) = rememberSaveable { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 10.dp)) {
        Text(text = "Sonido", modifier = Modifier.weight(.4f))
        Box(modifier = Modifier
            .weight(.6f)) {
            OutlinedTextField(
                value = if (indexSound == -1) "Default" else "Sonido ${indexSound + 1}",
                onValueChange = {},
                enabled = false,
                modifier = Modifier.clickable {
                    changeExpanded(true)
                },
                trailingIcon = {
                    Icon(painter = painterResource(id = R.drawable.ic_arrow_drop),
                        contentDescription = "")
                },
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { changeExpanded(false) },
            ) {
                DropdownMenuItem(
                    onClick = {
                        changeExpanded(false)
                        changeIndexSound(-1)
                    }
                ) {
                    Text(text = "Default")
                }
                (0..6).forEach {
                    DropdownMenuItem(
                        onClick = {
                            changeExpanded(false)
                            changeIndexSound(it)
                        }
                    ) {
                        Text(text = "Sonido ${it + 1}")
                    }
                }
            }
        }
    }

}


@Composable
fun RadioButtonNotifyType(
    changeNotify: (TypeNotify) -> Unit,
    currentNotify: TypeNotify,
) {
    val listType = listOf(
        TypeNotify.ALARM,
        TypeNotify.NOTIFY
    )
    listType.forEach {
        Row(modifier = Modifier
            .clickable { changeNotify(it) }
            .padding(horizontal = 15.dp)) {
            RadioButton(
                selected = it == currentNotify,
                onClick = null,
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(it.nameType)
                Spacer(modifier = Modifier.height(5.dp))
                Text(it.description, style = MaterialTheme.typography.caption)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }

}