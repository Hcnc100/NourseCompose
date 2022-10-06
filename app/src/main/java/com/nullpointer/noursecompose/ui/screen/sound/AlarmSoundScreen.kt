package com.nullpointer.noursecompose.ui.screen.sound

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.services.sound.SoundServicesControl
import com.nullpointer.noursecompose.ui.share.ImageAlarm
import com.nullpointer.noursecompose.ui.share.lottieFiles.LottieContainer

@Composable
fun AlarmSoundScreen(
    alarmSound: Alarm,
    orientation: Int = LocalConfiguration.current.orientation
) {
    Scaffold(
        floatingActionButton = { ButtonFinishServices() },
        floatingActionButtonPosition = FabPosition.Center,
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState()),
        ) {
            TitleAlarm(nameAlarm = alarmSound.name)
            when (orientation) {
                Configuration.ORIENTATION_PORTRAIT -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(70.dp)
                    ) {
                        ImageSoundAlarm(
                            imageAlarm = alarmSound.pathFile,
                            modifier = Modifier
                        )
                        if (alarmSound.description.isNotEmpty()) {
                            Text(text = alarmSound.description)
                        }
                    }
                }
                else -> {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ImageSoundAlarm(imageAlarm = alarmSound.pathFile)

                        if (alarmSound.description.isNotEmpty()) {
                            Text(
                                text = alarmSound.description,
                                modifier = Modifier.width(250.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ImageSoundAlarm(
    modifier: Modifier = Modifier,
    imageAlarm: String?
) {

    if (imageAlarm == null) {
        LottieContainer(
            modifier = modifier.size(200.dp),
            animation = R.raw.clock
        )
    } else {
        ImageAlarm(
            data = imageAlarm,
            modifier = modifier
                .size(200.dp)
                .clip(MaterialTheme.shapes.small)
        )
    }
}


@Composable
private fun TitleAlarm(
    nameAlarm: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(id = R.string.title_notify_alarm))
        Text(
            text = nameAlarm,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}

@Composable
private fun ButtonFinishServices(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        onClick = { SoundServicesControl.stopServices(context) },
        text = {
            Text(
                text = stringResource(R.string.name_action_stop_alarm),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        })
}