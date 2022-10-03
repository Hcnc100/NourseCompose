package com.nullpointer.noursecompose.ui.screen.alarms

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.actions.ActionItem
import com.nullpointer.noursecompose.actions.ActionItem.*
import com.nullpointer.noursecompose.core.states.Resource
import com.nullpointer.noursecompose.core.utils.shareViewModel
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.presentation.AlarmViewModel
import com.nullpointer.noursecompose.presentation.SelectionViewModel
import com.nullpointer.noursecompose.ui.dialogs.DialogDetails
import com.nullpointer.noursecompose.ui.interfaces.ActionRootDestinations
import com.nullpointer.noursecompose.ui.navigation.HomeNavGraph
import com.nullpointer.noursecompose.ui.screen.addAlarm.viewModel.AddAlarmViewModel
import com.nullpointer.noursecompose.ui.screen.alarms.componets.lists.ListEmptyAlarm
import com.nullpointer.noursecompose.ui.screen.alarms.componets.lists.ListLoadAlarm
import com.nullpointer.noursecompose.ui.screen.alarms.componets.lists.ListSuccessAlarm
import com.nullpointer.noursecompose.ui.screen.destinations.AddAlarmScreenDestination
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
    alarmViewModel: AlarmViewModel = shareViewModel(),
    addAlarmViewModel: AddAlarmViewModel = shareViewModel(),
    selectionViewModel: SelectionViewModel = shareViewModel(),
    alarmScreenState: AlarmScreenState = rememberAlarmScreenState()
) {
    val listAlarmState by alarmViewModel.listAlarm.collectAsState()
    var alarmSelected by rememberSaveable { mutableStateOf<Alarm?>(null) }

    BackHandler(selectionViewModel.isSelectedEnable, selectionViewModel::clearSelection)

    LaunchedEffect(key1 = Unit) {
        alarmViewModel.listAlarm.first { it is Resource.Success }.let { state ->
            val list = state as Resource.Success
            if (list.data.isEmpty()) {
                selectionViewModel.reselectedItemSelected(list.data)
            }
        }
    }

    AlarmScreen(
        alarmSelected = alarmSelected,
        stateListAlarm = listAlarmState,
        listState = alarmScreenState.lazyGridState,
        isSelectedEnable = selectionViewModel.isSelectedEnable,
        isScrollInProgress = alarmScreenState.isScrollInProgress,
        actionAlarm = { actionItem, alarm ->
            when (actionItem) {
                SIMPLE_CLICK_ITEM -> alarmSelected = alarm
                CHANGE_SELECT_ITEM -> alarm?.let { selectionViewModel.changeItemSelected(it) }
                REMOVE_LIST_SELECT -> {
                    alarmViewModel.deleterListAlarm(selectionViewModel.getListSelectionAndClear())
                }
                REMOVE_ITEM -> {
                    alarm?.let { alarmViewModel.deleterAlarm(it) }
                    alarmSelected = null
                }
                ADD_ITEM -> {
                    addAlarmViewModel.clearAlarmFields()
                    actionRootDestinations.changeRoot(AddAlarmScreenDestination)
                }
            }
        }
    )
}

@Composable
private fun AlarmScreen(
    alarmSelected: Alarm?,
    listState: LazyGridState,
    isSelectedEnable: Boolean,
    isScrollInProgress: Boolean,
    stateListAlarm: Resource<List<Alarm>>,
    actionAlarm: (ActionItem, Alarm?) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            ButtonToggleAddRemove(
                isVisible = !isScrollInProgress,
                isSelectedEnable = isSelectedEnable,
                actionAdd = { actionAlarm(ADD_ITEM, null) },
                actionRemove = { actionAlarm(REMOVE_LIST_SELECT, null) },
                descriptionButtonAdd = stringResource(R.string.description_add_alarm),
                descriptionButtonRemove = stringResource(R.string.description_remove_alarms)
            )
        }
    ) { paddingValues ->
        ListAlarm(
            listState = listState,
            listAlarm = stateListAlarm,
            isSelectedEnable = isSelectedEnable,
            modifier = Modifier.padding(paddingValues),
            changeItemSelect = { actionAlarm(CHANGE_SELECT_ITEM, it as Alarm) },
            simpleClickAlarm = { actionAlarm(SIMPLE_CLICK_ITEM, it) }
        )
    }

    alarmSelected?.let {
        DialogDetails(
            alarm = it,
            actionHiddenDialog = { actionAlarm(SIMPLE_CLICK_ITEM, null) },
            deleterAlarm = { actionAlarm(REMOVE_ITEM, it) }
        )
    }
}

@Composable
private fun ListAlarm(
    isSelectedEnable: Boolean,
    listState: LazyGridState,
    modifier: Modifier = Modifier,
    listAlarm: Resource<List<Alarm>>,
    simpleClickAlarm: (Alarm) -> Unit,
    changeItemSelect: (ItemSelected) -> Unit
) {
    when (listAlarm) {
        is Resource.Success -> {
            if (listAlarm.data.isEmpty()) {
                ListEmptyAlarm(modifier = modifier)
            } else {
                ListSuccessAlarm(
                    listState = listState,
                    listAlarm = listAlarm.data,
                    isSelectedEnable = isSelectedEnable,
                    changeSelectState = changeItemSelect,
                    simpleClickAlarm = simpleClickAlarm,
                    modifier = modifier
                )
            }
        }
        else -> ListLoadAlarm(modifier = modifier)
    }
}