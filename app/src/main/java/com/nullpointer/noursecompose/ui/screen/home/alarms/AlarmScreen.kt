package com.nullpointer.noursecompose.ui.screen.home.alarms

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.presentation.AlarmViewModel
import com.nullpointer.noursecompose.presentation.SelectionViewModel
import com.nullpointer.noursecompose.ui.screen.destinations.AddAlarmScreenDestination
import com.nullpointer.noursecompose.ui.share.ButtonToggleAddRemove
import com.nullpointer.noursecompose.ui.share.backHandler.BackHandler
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class)
@Destination(start = true)
@Composable
fun AlarmScreen(
    alarmViewModel: AlarmViewModel = hiltViewModel(),
    selectionViewModel: SelectionViewModel,
    navigator: DestinationsNavigator,
) {
    val listAlarm = alarmViewModel.listAlarm.collectAsState().value
    Scaffold(
        floatingActionButton = {
            ButtonToggleAddRemove(isVisible = true,
                isSelectedEnable = false,
                descriptionButtonAdd = "",
                actionAdd = { navigator.navigate(AddAlarmScreenDestination) },
                descriptionButtonRemove = "", actionRemove = {})
        }
    ) {
        LazyVerticalGrid(cells = GridCells.Adaptive(150.dp)) {
            items(listAlarm.size) { index ->
                ItemAlarm(alarm = listAlarm[index],
                    selectionViewModel.isSelectedEnable,
                    selectionViewModel::changeItemSelected)
            }

        }
    }

    BackHandler(selectionViewModel.isSelectedEnable) {
        selectionViewModel.clearSelection()
    }
}
