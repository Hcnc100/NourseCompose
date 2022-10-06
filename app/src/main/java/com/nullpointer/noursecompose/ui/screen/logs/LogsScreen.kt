package com.nullpointer.noursecompose.ui.screen.logs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.actions.ActionLogScreen
import com.nullpointer.noursecompose.actions.ActionLogScreen.*
import com.nullpointer.noursecompose.core.states.Resource
import com.nullpointer.noursecompose.core.utils.shareViewModel
import com.nullpointer.noursecompose.models.ItemSelected
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.registry.Log
import com.nullpointer.noursecompose.presentation.AlarmViewModel
import com.nullpointer.noursecompose.presentation.LogViewModel
import com.nullpointer.noursecompose.presentation.SelectionViewModel
import com.nullpointer.noursecompose.ui.dialogs.DialogDetails
import com.nullpointer.noursecompose.ui.interfaces.ActionRootDestinations
import com.nullpointer.noursecompose.ui.navigation.MainNavGraph
import com.nullpointer.noursecompose.ui.screen.logs.componets.list.LoadLogsScreen
import com.nullpointer.noursecompose.ui.screen.logs.componets.list.SuccessLogsScreen
import com.nullpointer.noursecompose.ui.share.FabAnimation
import com.nullpointer.noursecompose.ui.share.mpGraph.ToolbarBack
import com.nullpointer.noursecompose.ui.states.SelectedScreenState
import com.nullpointer.noursecompose.ui.states.rememberSelectedScreenState
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@MainNavGraph
@Destination
@Composable
fun LogsScreens(
    logViewModel: LogViewModel = hiltViewModel(),
    actionRootDestinations: ActionRootDestinations,
    alarmViewModel: AlarmViewModel = shareViewModel(),
    selectionViewModel: SelectionViewModel = shareViewModel(),
    logsScreenState: SelectedScreenState = rememberSelectedScreenState()
) {
    val listLogs by logViewModel.listLogs.collectAsState()
    var alarmSelected by rememberSaveable { mutableStateOf<Alarm?>(null) }

    BackHandler(
        enabled = selectionViewModel.isSelectedEnable,
        onBack = selectionViewModel::clearSelection
    )

    LogsScreen(
        listLogState = listLogs,
        alarmSelected = alarmSelected,
        lazyGridState = logsScreenState.lazyGridState,
        isDeleterEnabled = selectionViewModel.isSelectedEnable && !logsScreenState.isScrollInProgress,
        isSelectedEnable = !logsScreenState.isScrollInProgress && selectionViewModel.isSelectedEnable,
        actionLogScreen = { actionLogScreen, log ->
            when (actionLogScreen) {
                ACTION_BACK -> actionRootDestinations.backDestination()
                HIDDEN_LOG_DIALOG -> {
                    alarmSelected = null
                }
                SELECTED_LOG -> {
                    log?.let { selectionViewModel.changeItemSelected(it) }
                }
                DELETER_LIST_LOG -> {
                    logViewModel.deleterRegistry(selectionViewModel.getListSelectionAndClear())
                }
                DELETER_ALARM -> {
                    alarmSelected?.let {
                        alarmViewModel.deleterAlarm(it)
                        alarmSelected = null
                    }
                }
                SHOW_LOG_DIALOG -> {
                    log?.let {
                        logsScreenState.coroutineScope.launch {
                            val alarm = alarmViewModel.getAlarmById(it.idAlarm)
                            if (alarm != null) {
                                alarmSelected = alarm
                            } else {
                                logsScreenState.showToast(R.string.message_alarm_deleter)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun LogsScreen(
    alarmSelected: Alarm?,
    isDeleterEnabled: Boolean,
    actionLogScreen: (ActionLogScreen, Log?) -> Unit,
    isSelectedEnable: Boolean,
    lazyGridState: LazyGridState,
    listLogState: Resource<List<Log>>
) {

    Scaffold(
        topBar = {
            ToolbarBack(
                title = stringResource(R.string.title_logs_screen),
                actionBack = { actionLogScreen(ACTION_BACK, null) }
            )
        },
        floatingActionButton = {
            FabAnimation(
                isVisible = isDeleterEnabled,
                icon = Icons.Default.Delete,
                description = stringResource(R.string.description_deleter_registry),
                action = { actionLogScreen(DELETER_LIST_LOG, null) }
            )
        }
    ) { paddingValues ->
        LogListState(
            modifier = Modifier.padding(paddingValues),
            isSelectEnable = isSelectedEnable,
            listState = lazyGridState,
            listLogState = listLogState,
            actionSelect = { actionLogScreen(SELECTED_LOG, it as Log) },
            actionSimpleClick = { actionLogScreen(SHOW_LOG_DIALOG, it) }
        )
    }


    alarmSelected?.let {
        DialogDetails(
            alarm = alarmSelected,
            actionHiddenDialog = { actionLogScreen(HIDDEN_LOG_DIALOG, null) },
            deleterAlarm = { actionLogScreen(DELETER_ALARM, null) }
        )
    }
}


@Composable
fun LogListState(
    isSelectEnable: Boolean,
    listState: LazyGridState,
    modifier: Modifier = Modifier,
    actionSimpleClick: (Log) -> Unit,
    listLogState: Resource<List<Log>>,
    actionSelect: (ItemSelected) -> Unit
) {
    when (listLogState) {
        is Resource.Success -> {
            SuccessLogsScreen(
                modifier = modifier,
                lazyGridState = listState,
                listLog = listLogState.data,
                isSelectEnable = isSelectEnable,
                changeItemSelected = actionSelect,
                actionClickLog = actionSimpleClick
            )
        }
        else -> LoadLogsScreen(modifier = modifier)
    }
}