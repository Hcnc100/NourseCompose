package com.nullpointer.noursecompose.ui.screen.statusAlarm

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.ui.navigation.MainNavGraph
import com.nullpointer.noursecompose.ui.screen.statusAlarm.componets.SimpleItemAlarm
import com.nullpointer.noursecompose.ui.screen.statusAlarm.viewModel.StatusAlarmsViewModel
import com.nullpointer.noursecompose.ui.share.mpGraph.ToolbarBack
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination

@MainNavGraph
@Destination(
    deepLinks = [
        DeepLink(uriPattern = "https://www.nourse-compose.com/alarm/{args}")
    ]
)
@Composable
fun StatusAlarm(
//    args: ArgsAlarms,
    statusViewModel: StatusAlarmsViewModel = hiltViewModel(),
) {
//    LaunchedEffect(key1 = Unit) {
//        statusViewModel.initAlarms(args.listIdAlarm)
//    }
    val listAlarms = statusViewModel.listIdsAlarm.value
    Scaffold(
        topBar = {
//            ToolbarBack(title =
//            if (args.isLost) stringResource(R.string.title_lost_alarm) else stringResource(R.string.title_list_alarm))
        }
    ) {
        when (listAlarms) {
            null -> Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            else -> {
//                LazyVerticalGrid(cells = GridCells.Adaptive(250.dp)) {
//                    if (listAlarms.size > 1)
//                        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
//                            Text(text = stringResource(R.string.text_no_take_one_more),
//                                style = MaterialTheme.typography.caption,
//                                color = MaterialTheme.colors.error,
//                                modifier = Modifier.padding(vertical = 15.dp, horizontal = 10.dp))
//                        }
//                    items(listAlarms.size) { index ->
//                        SimpleItemAlarm(alarm = listAlarms[index])
//                    }
                }
            }
    }
}