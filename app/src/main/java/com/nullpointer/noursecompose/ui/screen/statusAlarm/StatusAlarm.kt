package com.nullpointer.noursecompose.ui.screen.statusAlarm

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.core.utils.shareViewModel
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.presentation.AlarmViewModel
import com.nullpointer.noursecompose.ui.dialogs.DialogDetails
import com.nullpointer.noursecompose.ui.interfaces.ActionRootDestinations
import com.nullpointer.noursecompose.ui.navigation.MainNavGraph
import com.nullpointer.noursecompose.ui.navigation.types.ArgsAlarms
import com.nullpointer.noursecompose.ui.screen.statusAlarm.componets.SimpleItemAlarm
import com.nullpointer.noursecompose.ui.share.ToolbarBack
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.FULL_ROUTE_PLACEHOLDER

@MainNavGraph
@Destination(
    deepLinks = [
        DeepLink(uriPattern = "https://www.nourse-compose.com/$FULL_ROUTE_PLACEHOLDER")
    ]
)
@Composable
fun StatusAlarm(
    args: ArgsAlarms,
    actionRootDestinations: ActionRootDestinations,
    alarmViewModel: AlarmViewModel = shareViewModel()
) {
    val (alarmSelected, changeAlarmSelected) = rememberSaveable { mutableStateOf<Alarm?>(null) }
    val title by remember {
        derivedStateOf {
            if (args.isLost) R.string.title_lost_alarm else R.string.title_list_alarm
        }
    }
    Scaffold(
        topBar = {
            ToolbarBack(
                title = stringResource(id = title),
                actionBack = actionRootDestinations::backDestination
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = Modifier.padding(paddingValues)
        ) {

            item(span = { GridItemSpan(maxCurrentLineSpan) }, key = "key-title-count") {
                if (args.listIdAlarm.size > 1)
                    Text(
                        text = stringResource(R.string.text_no_take_one_more),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(vertical = 15.dp, horizontal = 10.dp)
                    )
            }
            items(args.listIdAlarm, key = { it.id }) { alarm ->
                SimpleItemAlarm(
                    alarm = alarm,
                    clickAlarm = changeAlarmSelected)
            }
        }
    }

    if (alarmSelected != null) {
        DialogDetails(
            alarm = alarmSelected,
            actionHiddenDialog = { changeAlarmSelected(null) },
            deleterAlarm = {
                alarmViewModel.deleterAlarm(it)
                changeAlarmSelected(null)
            }
        )
    }
}
