package com.nullpointer.noursecompose.ui.activitys

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.turnScreenOffAndKeyguardOn
import com.nullpointer.noursecompose.core.utils.turnScreenOnAndKeyguardOff
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.services.SoundServices
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
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            onClick = {
                                SoundServices.stopServices(context)
                                context.finish()
                            },
                            text = {
                                Text(text = getString(R.string.name_action_stop_alarm),
                                    modifier = Modifier.padding(horizontal = 20.dp))
                            })
                    },
                ) {

                    when (LocalConfiguration.current.orientation) {
                        Configuration.ORIENTATION_PORTRAIT -> {
                            Column(modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(stringResource(id = R.string.title_notify_alarm))
                                Text(alarm.name,
                                    style = MaterialTheme.typography.h5,
                                    modifier = Modifier.padding(vertical = 10.dp))

//                                if (alarm.nameFile == null) {
//                                    LottieContainer(modifier = Modifier.size(250.dp),
//                                        animation = R.raw.clock)
//                                } else {
//
//                                }

                                Spacer(modifier = Modifier.height(70.dp))

                                if (alarm.description.isNotEmpty()) {
                                    Text(text = alarm.description)
                                }
                            }
                        }
                        else -> {
                            Column(modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(stringResource(id = R.string.title_notify_alarm))
                                Text(alarm.name,
                                    style = MaterialTheme.typography.h5,
                                    modifier = Modifier.padding(vertical = 10.dp))
                                Row {
                                    val modifier = Modifier
                                        .size(250.dp)
                                        .weight(4f)
//                                    if (alarm.nameFile == null) {
//                                        LottieContainer(modifier = modifier,
//                                            animation = R.raw.clock)
//                                    } else {
//                                        ImageAlarm(
//                                            contentDescription = getString(R.string.description_img_current_alarm),
//                                            modifier = modifier,
//                                            urlImg = alarm.nameFile,
//                                            contentScale = ContentScale.Crop
//                                        )
//                                    }
                                    if (alarm.description.isNotEmpty()) {
                                        Text(text = alarm.description, modifier = Modifier.weight(6f))
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