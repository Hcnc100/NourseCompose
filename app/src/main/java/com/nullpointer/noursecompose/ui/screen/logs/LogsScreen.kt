package com.nullpointer.noursecompose.ui.screen.logs

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.R
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
    alarmViewModel: AlarmViewModel = shareViewModel(),
    selectionViewModel: SelectionViewModel = shareViewModel(),
    actionRootDestinations: ActionRootDestinations,
    logViewModel: LogViewModel = hiltViewModel(),
    logsScreenState: SelectedScreenState = rememberSelectedScreenState()
) {
    val listAlarm by logViewModel.listLogs.collectAsState()
    val (alarmSelected, changeAlarmSelected) = rememberSaveable { mutableStateOf<Alarm?>(null) }

    BackHandler(
        enabled = selectionViewModel.isSelectedEnable,
        onBack = selectionViewModel::clearSelection
    )

    Scaffold(
        topBar = {
            ToolbarBack(
                title = stringResource(R.string.title_logs_screen),
                actionBack = actionRootDestinations::backDestination
            )
        },
        floatingActionButton = {
            FabAnimation(
                isVisible = selectionViewModel.isSelectedEnable && !logsScreenState.isScrollInProgress,
                icon = Icons.Default.Delete,
                description = stringResource(R.string.description_deleter_registry),
                action = {
                    logViewModel.deleterRegistry(selectionViewModel.getListSelectionAndClear())
                }
            )
        }
    ) {
        LogsScreenListState(
            listLogState = listAlarm,
            listState = logsScreenState.lazyGridState,
            actionSimpleClick = {
                logsScreenState.coroutineScope.launch {
                    val alarm = alarmViewModel.getAlarmById(it.idAlarm)
                    if (alarm != null) {
                        changeAlarmSelected(alarm)
                    } else {
                        logsScreenState.showToast(R.string.message_alarm_deleter)
                    }
                }
            },
            actionSelect = selectionViewModel::changeItemSelected,
            isSelectEnable = selectionViewModel.isSelectedEnable,
            modifier = Modifier.padding(it)
        )
    }

    if (alarmSelected != null) {
        DialogDetails(
            alarm = alarmSelected,
            actionHiddenDialog = { changeAlarmSelected(null) },
            deleterAlarm = {
                alarmViewModel.deleterAlarm(alarmSelected)
                changeAlarmSelected(null)
            }
        )
    }
}

@Composable
fun LogsScreenListState(
    listLogState: Resource<List<Log>>,
    listState: LazyGridState,
    modifier: Modifier = Modifier,
    actionSimpleClick: (Log) -> Unit,
    actionSelect: (ItemSelected) -> Unit,
    isSelectEnable: Boolean
) {
    when (listLogState) {
        is Resource.Success -> {
            LazyVerticalGrid(
                state = listState,
                modifier = modifier,
                columns = GridCells.Adaptive(dimensionResource(id = R.dimen.size_row_log))
            ) {
                items(listLogState.data) { log ->
                    ItemLog(
                        registry = log,
                        actionClick = actionSimpleClick,
                        changeItemSelected = actionSelect,
                        isSelectEnable = isSelectEnable
                    )
                }
            }
        }
        else -> LoadingLogsScreen(modifier = modifier)
    }
}