package com.nullpointer.noursecompose.ui.screen.config

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.notify.TypeNotify
import com.nullpointer.noursecompose.ui.screen.addAlarm.timeScreen.TextMiniTitle
import com.nullpointer.noursecompose.ui.screen.config.viewModel.ConfigViewModel
import com.nullpointer.noursecompose.ui.share.mpGraph.ToolbarBack
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun ConfigScreen(
    configViewModel: ConfigViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val typeNotify = configViewModel.typeNotify.collectAsState()
    val indexSound = configViewModel.intSound.collectAsState()

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
                changeIndexSound = configViewModel::changeIntSound)
            Spacer(modifier = Modifier.height(5.dp))
            Row {
               Box(modifier = Modifier.weight(.4f))
                TextButton(modifier = Modifier
                    .weight(.6f),onClick = { }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painterResource(id = R.drawable.ic_play), contentDescription ="")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Preview")
                    }
                }
            }
        }
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