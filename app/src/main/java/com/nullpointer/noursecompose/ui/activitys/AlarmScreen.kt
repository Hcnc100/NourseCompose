package com.nullpointer.noursecompose.ui.activitys

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.turnScreenOffAndKeyguardOn
import com.nullpointer.noursecompose.core.utils.turnScreenOnAndKeyguardOff
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.services.SoundServices
import com.nullpointer.noursecompose.ui.share.ImageAlarm
import com.nullpointer.noursecompose.ui.share.lottieFiles.LottieContainer
import com.nullpointer.noursecompose.ui.theme.NourseComposeTheme


class AlarmScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        turnScreenOnAndKeyguardOff()
        val alarm = intent.extras?.getParcelable<Alarm>(SoundServices.KEY_ALARM_PASS_ACTIVITY)!!
        setContent {
            NourseComposeTheme {
                val context = LocalContext.current as AppCompatActivity
                val alarmIsSound = SoundServices.alarmIsAlive

                LaunchedEffect(key1 = alarmIsSound) {
                    if (!alarmIsSound) context.finish()
                }

                Scaffold(
                    floatingActionButtonPosition = FabPosition.Center,
                    floatingActionButton = { ButtonFinishServices() },
                ) {

                    when (LocalConfiguration.current.orientation) {
                        Configuration.ORIENTATION_PORTRAIT -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(it)
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                            ) {
                                TitleAlarm(nameAlarm = alarm.name)
                                ImageSoundAlarm(
                                    imageAlarm = alarm.pathFile,
                                    modifier = Modifier
                                        .size(250.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                )
                                Spacer(modifier = Modifier.height(70.dp))
                                if (alarm.description.isNotEmpty()) {
                                    Text(text = alarm.description)
                                }
                            }
                        }
                        else -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(it)
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                            ) {
                                TitleAlarm(nameAlarm = alarm.name)
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    ImageSoundAlarm(
                                        imageAlarm = alarm.pathFile, modifier = Modifier
                                            .size(200.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .weight(4f)
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))
                                    if (alarm.description.isNotEmpty()) {
                                        Text(
                                            text = alarm.description,
                                            modifier = Modifier.weight(6f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        turnScreenOffAndKeyguardOn()
    }
}

@Composable
private fun ImageSoundAlarm(
    modifier: Modifier = Modifier,
    imageAlarm: String?
) {
    if (imageAlarm == null) {
        LottieContainer(
            modifier = modifier,
            animation = R.raw.clock
        )
    } else {
        ImageAlarm(
            path = imageAlarm,
            modifier = modifier
        )
    }
}

@Composable
private fun TitleAlarm(nameAlarm: String, modifier: Modifier = Modifier) {
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
    context: AppCompatActivity = LocalContext.current as AppCompatActivity
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        onClick = {
            SoundServices.stopServices(context)
            context.finish()
        },
        text = {
            Text(
                text = stringResource(R.string.name_action_stop_alarm),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        })
}