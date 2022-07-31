package com.nullpointer.noursecompose.ui.screen.logs

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.presentation.AlarmViewModel
import com.nullpointer.noursecompose.presentation.LogViewModel
import com.nullpointer.noursecompose.ui.dialogs.DialogDetails
import com.nullpointer.noursecompose.ui.navigation.MainNavGraph
import com.nullpointer.noursecompose.ui.screen.destinations.AddAlarmScreenDestination
import com.nullpointer.noursecompose.ui.screen.empty.EmptyScreen
import com.nullpointer.noursecompose.ui.screen.logs.componets.ItemLog
import com.nullpointer.noursecompose.ui.share.FabAnimation
import com.nullpointer.noursecompose.ui.share.mpGraph.ToolbarBack
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@MainNavGraph
@Destination
@Composable
fun LogsScreens(
    logViewModel: LogViewModel = hiltViewModel(),
    alarmViewModel: AlarmViewModel,
    navigator: DestinationsNavigator,
) {
    val listAlarm = logViewModel.listLogs.collectAsState().value
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val stateScroll = rememberLazyGridState()
    val (alarmSelected, changeAlarmSelected) = rememberSaveable { mutableStateOf<Alarm?>(null) }
    Scaffold(
        topBar = {
            ToolbarBack(title = stringResource(R.string.title_logs_screen), navigator::popBackStack
            )
        },
        floatingActionButton = {
            FabAnimation(isVisible = listAlarm?.isNotEmpty() == true && !stateScroll.isScrollInProgress,
                icon = Icons.Default.Delete,
                description = stringResource(
                    R.string.description_deleter_registry),
                action = logViewModel::deleterAllRegistry)
        }
    ) {
        when {

            listAlarm == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            listAlarm.isEmpty() -> {
                EmptyScreen(animation = R.raw.empty3,
                    textEmpty = stringResource(R.string.text_empty_registry))
            }
            else -> {
//                LazyVerticalGrid(state = stateScroll, cells = GridCells.Adaptive(180.dp)) {
//                    items(listAlarm.size) { index ->
//                        ItemLog(registry = listAlarm[index],
//                            actionClick = {
//                                scope.launch {
//                                    val alarm = alarmViewModel.getAlarmById(it)
//                                    if (alarm != null) {
//                                        changeAlarmSelected(alarm)
//                                    } else {
//                                        Toast.makeText(context,
//                                            context.getString(R.string.message_alarm_deleter),
//                                            Toast.LENGTH_SHORT).show()
//                                    }
//                                }
//                            }
//                        )
//                    }
//                }
            }
        }
    }

    if (alarmSelected != null) {
        DialogDetails(
            alarm = alarmSelected,
            actionHiddenDialog = { changeAlarmSelected(null) },
            actionEditAlarm = {
//                navigator.navigate(AddAlarmScreenDestination.invoke(it))
                              },
            deleterAlarm = {
//                alarmViewModel.deleterAlarm(it, context)
                changeAlarmSelected(null)
            }
        )
    }
}