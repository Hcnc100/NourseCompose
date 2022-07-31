package com.nullpointer.noursecompose.ui.screen.alarms

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.states.Resource
import com.nullpointer.noursecompose.core.utils.shareViewModel
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.presentation.AlarmViewModel
import com.nullpointer.noursecompose.presentation.SelectionViewModel
import com.nullpointer.noursecompose.ui.dialogs.DialogDetails
import com.nullpointer.noursecompose.ui.interfaces.ActionRootDestinations
import com.nullpointer.noursecompose.ui.navigation.HomeNavGraph
import com.nullpointer.noursecompose.ui.screen.destinations.AddAlarmScreenDestination
import com.nullpointer.noursecompose.ui.screen.empty.EmptyScreen
import com.nullpointer.noursecompose.ui.share.ButtonToggleAddRemove
import com.nullpointer.noursecompose.ui.states.AlarmScreenState
import com.nullpointer.noursecompose.ui.states.rememberAlarmScreenState
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.first

@HomeNavGraph(start = true)
@Destination
@Composable
fun AlarmScreen(
    actionRootDestinations: ActionRootDestinations,
    selectionViewModel: SelectionViewModel,
    alarmViewModel: AlarmViewModel = shareViewModel(),
    alarmScreenState: AlarmScreenState = rememberAlarmScreenState()
) {
    val listAlarmState by alarmViewModel.listAlarm.collectAsState()
    val (alarmSelected, changeAlarmSelected) = rememberSaveable { mutableStateOf<Alarm?>(null) }

    BackHandler(selectionViewModel.isSelectedEnable, selectionViewModel::clearSelection)

    LaunchedEffect(key1 = Unit) {
        alarmViewModel.listAlarm.first { it is Resource.Success }.let { state ->
            val list = state as Resource.Success
            if (list.data.isEmpty()) {
                selectionViewModel.reselectedItemSelected(list.data)
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            ButtonToggleAddRemove(
                isVisible = !alarmScreenState.isScrollInProgress,
                isSelectedEnable = selectionViewModel.isSelectedEnable,
                descriptionButtonRemove = stringResource(R.string.description_remove_alarms),
                descriptionButtonAdd = stringResource(R.string.description_add_alarm),
                actionAdd = { actionRootDestinations.changeRoot(AddAlarmScreenDestination) },
                actionRemove = { alarmViewModel.deleterListAlarm(selectionViewModel.getListSelectionAndClear()) }
            )
        }
    ) { paddingValues ->
        ListAlarm(
            listAlarm = listAlarmState,
            isSelectedEnable = selectionViewModel.isSelectedEnable,
            listState = alarmScreenState.lazyGridState,
            changeItemSelect = selectionViewModel::changeItemSelected,
            simpleClickAlarm = { changeAlarmSelected(it) },
            modifier = Modifier.padding(paddingValues)
        )
    }
    if (alarmSelected != null) {
        DialogDetails(
            alarm = alarmSelected,
            actionHiddenDialog = { changeAlarmSelected(null) },
            actionEditAlarm = {
//                actionRootDestinations.changeRoot(AddAlarmScreenDestination)
            },
            deleterAlarm = {
                alarmViewModel.deleterAlarm(it)
                changeAlarmSelected(null)
            }
        )
    }
}

@Composable
private fun ListAlarm(
    listAlarm: Resource<List<Alarm>>,
    isSelectedEnable: Boolean,
    listState: LazyGridState,
    changeItemSelect: (ItemSelected) -> Unit,
    simpleClickAlarm: (Alarm) -> Unit,
    modifier: Modifier = Modifier
) {
    when (listAlarm) {
        is Resource.Success -> {
            if (listAlarm.data.isEmpty()) {
                EmptyScreen(
                    modifier = modifier,
                    animation = R.raw.empty3,
                    textEmpty = stringResource(R.string.message_empty_alarm_screen)
                )
            } else {
                LazyVerticalGrid(
                    modifier = modifier,
                    state = listState,
                    columns = GridCells.Adaptive(150.dp)
                ) {
                    items(listAlarm.data, key = { it.id }) { alarm ->
                        ItemAlarm(
                            alarm = alarm,
                            isSelectedEnable = isSelectedEnable,
                            changeSelectState = changeItemSelect,
                            actionClickSimple = simpleClickAlarm
                        )
                    }
                }
            }
        }
        else -> {}
    }
}