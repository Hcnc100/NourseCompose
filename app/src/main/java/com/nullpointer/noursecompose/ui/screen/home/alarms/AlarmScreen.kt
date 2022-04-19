package com.nullpointer.noursecompose.ui.screen.home.alarms

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyGridState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.presentation.AlarmViewModel
import com.nullpointer.noursecompose.presentation.SelectionViewModel
import com.nullpointer.noursecompose.ui.dialogs.DialogDetails
import com.nullpointer.noursecompose.ui.screen.destinations.AddAlarmScreenDestination
import com.nullpointer.noursecompose.ui.screen.empty.EmptyScreen
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
    val context = LocalContext.current
    val listState = rememberLazyGridState()
    val (isShowDialog, changeShowDialog) = rememberSaveable { mutableStateOf(false) }
    val (alarmSelected, changeAlarmSelected) = rememberSaveable { mutableStateOf<Alarm?>(null) }
    Scaffold(
        floatingActionButton = {
            ButtonToggleAddRemove(isVisible = !listState.isScrollInProgress,
                isSelectedEnable = selectionViewModel.isSelectedEnable,
                descriptionButtonAdd = "",
                actionAdd = { navigator.navigate(AddAlarmScreenDestination) },
                descriptionButtonRemove = "", actionRemove = {
                    alarmViewModel.deleterListAlarm(
                        selectionViewModel.getListSelectionAndClear(), context
                    )
                })
        }
    ) {
        when {
            listAlarm == null ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

            listAlarm.isEmpty() ->
                EmptyScreen(animation = R.raw.empty3, textEmpty = "No hay alarmas guardadas")

            listAlarm.isNotEmpty() ->
                LazyVerticalGrid(cells = GridCells.Adaptive(150.dp), state = listState) {
                    items(listAlarm.size) { index ->
                        ItemAlarm(alarm = listAlarm[index],
                            isSelectedEnable = selectionViewModel.isSelectedEnable,
                            changeSelectState = selectionViewModel::changeItemSelected,
                            actionClickSimple = {
                                changeAlarmSelected(it)
                                changeShowDialog(true)
                            }
                        )
                    }
                }

        }
    }
    if (alarmSelected != null && isShowDialog) {
        DialogDetails(
            alarm = alarmSelected,
            actionHiddenDialog = {
                changeShowDialog(false)
                changeAlarmSelected(null)
            }
        )
    }

    BackHandler(selectionViewModel.isSelectedEnable) {
        selectionViewModel.clearSelection()
    }
}
