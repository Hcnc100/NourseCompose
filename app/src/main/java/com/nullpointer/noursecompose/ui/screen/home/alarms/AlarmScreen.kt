package com.nullpointer.noursecompose.ui.screen.home.alarms

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyGridState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.ItemSelected
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
    val listAlarmState = alarmViewModel.listAlarm.collectAsState()
    val context = LocalContext.current
    val listState = rememberLazyGridState()
    val (alarmSelected, changeAlarmSelected) = rememberSaveable { mutableStateOf<Alarm?>(null) }

    Scaffold(
        floatingActionButton = {
            ButtonToggleAddRemove(isVisible = !listState.isScrollInProgress,
                isSelectedEnable = selectionViewModel.isSelectedEnable,
                descriptionButtonAdd = stringResource(R.string.description_add_alarm),
                actionAdd = { navigator.navigate(AddAlarmScreenDestination.invoke()) },
                descriptionButtonRemove = stringResource(R.string.description_remove_alarms),
                actionRemove = {
                    alarmViewModel.deleterListAlarm(
                        selectionViewModel.getListSelectionAndClear(), context
                    )
                })
        }
    ) {
        ListAlarm(
            listAlarm = listAlarmState.value,
            isSelectedEnable = selectionViewModel.isSelectedEnable,
            listState = listState,
            changeItemSelect = selectionViewModel::changeItemSelected,
            simpleClickAlarm = {
                changeAlarmSelected(it)
            })
    }
    if (alarmSelected != null) {
        DialogDetails(
            alarm = alarmSelected,
            actionHiddenDialog = {
                changeAlarmSelected(null)
            },
            actionEditAlarm = { navigator.navigate(AddAlarmScreenDestination.invoke(it)) },
            deleterAlarm = {
                alarmViewModel.deleterAlarm(it, context)
                changeAlarmSelected(null)
            }
        )
    }

    BackHandler(selectionViewModel.isSelectedEnable) {
        selectionViewModel.clearSelection()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListAlarm(
    listAlarm: List<Alarm>?,
    isSelectedEnable: Boolean,
    listState: LazyGridState,
    changeItemSelect: (ItemSelected) -> Unit,
    simpleClickAlarm: (Alarm) -> Unit,
) {
    when {
        listAlarm == null ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

        listAlarm.isEmpty() ->
            EmptyScreen(animation = R.raw.empty3,
                textEmpty = stringResource(R.string.message_empty_alarm_screen))

        listAlarm.isNotEmpty() ->
            LazyVerticalGrid(cells = GridCells.Adaptive(150.dp), state = listState) {
                items(listAlarm.size, key = { listAlarm[it].id ?: 0 }) { index ->
                    ItemAlarm(alarm = listAlarm[index],
                        isSelectedEnable = isSelectedEnable,
                        changeSelectState = changeItemSelect,
                        actionClickSimple = simpleClickAlarm
                    )
                }
            }
    }


}