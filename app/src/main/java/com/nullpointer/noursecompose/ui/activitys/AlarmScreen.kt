package com.nullpointer.noursecompose.ui.activitys

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.ImageUtils
import com.nullpointer.noursecompose.core.utils.turnScreenOffAndKeyguardOn
import com.nullpointer.noursecompose.core.utils.turnScreenOnAndKeyguardOff
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.services.AlarmReceiver
import com.nullpointer.noursecompose.services.SoundServices
import com.nullpointer.noursecompose.services.SoundServices.Companion.KEY_ALARM_PASS
import com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen.ImageAlarm
import com.nullpointer.noursecompose.ui.share.lottieFiles.LottieContainer
import com.nullpointer.noursecompose.ui.theme.NourseComposeTheme

class AlarmScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alarm = intent.extras?.getParcelable<Alarm>(SoundServices.KEY_ALARM_PASS_ACTIVITY)!!
        turnScreenOnAndKeyguardOff()
        setContent {
            NourseComposeTheme {

                val context = LocalContext.current as AppCompatActivity
                Scaffold(
                    floatingActionButtonPosition = FabPosition.Center,
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            onClick = {
                                SoundServices.stopServices(context)
                                context.finish()
                            },
                            text = {
                                Text(text = "Descatar",
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
                                Text("Recordatorio de medicamento")
                                Text(alarm.title,
                                    style = MaterialTheme.typography.h5,
                                    modifier = Modifier.padding(vertical = 10.dp))

                                if (alarm.nameFile == null) {
                                    LottieContainer(modifier = Modifier.size(250.dp),
                                        animation = R.raw.clock)
                                } else {
                                    ImageAlarm(
                                        contentDescription = "",
                                        modifier = Modifier.size(250.dp),
                                        urlImg = alarm.nameFile,
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                Spacer(modifier = Modifier.height(70.dp))

                                if (alarm.message.isNotEmpty()) {
                                    Text(text = alarm.message)
                                }
                            }
                        }
                        else -> {
                            Column(modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Recordatorio de medicamento")
                                Text(alarm.title,
                                    style = MaterialTheme.typography.h5,
                                    modifier = Modifier.padding(vertical = 10.dp))
                                Row {
                                    val modifier = Modifier
                                        .size(250.dp)
                                        .weight(4f)
                                    if (alarm.nameFile == null) {
                                        LottieContainer(modifier = modifier,
                                            animation = R.raw.clock)
                                    } else {
                                        ImageAlarm(
                                            contentDescription = "",
                                            modifier = modifier,
                                            urlImg = alarm.nameFile,
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    if (alarm.message.isNotEmpty()) {
                                        Text(text = alarm.message, modifier = Modifier.weight(6f))
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