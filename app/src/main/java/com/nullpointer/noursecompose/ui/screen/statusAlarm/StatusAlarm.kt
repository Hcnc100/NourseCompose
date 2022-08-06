package com.nullpointer.noursecompose.ui.screen.statusAlarm

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nullpointer.noursecompose.R
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
    actionRootDestinations: ActionRootDestinations
) {
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
            columns = GridCells.Adaptive(250.dp),
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
                SimpleItemAlarm(alarm = alarm)
            }
        }
    }
}
