package com.nullpointer.noursecompose.ui.screen.home.alarms

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.nullpointer.noursecompose.ui.screen.destinations.AddAlarmScreenDestination
import com.nullpointer.noursecompose.ui.share.ButtonToggleAddRemove
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun AlarmScreen(
    navigator: DestinationsNavigator,
) {
    Scaffold(
        floatingActionButton = {
            ButtonToggleAddRemove(isVisible = true,
                isSelectedEnable = false,
                descriptionButtonAdd = "",
                actionAdd = { navigator.navigate(AddAlarmScreenDestination) },
                descriptionButtonRemove = "", actionRemove = {})
        }
    ) {

    }
}
