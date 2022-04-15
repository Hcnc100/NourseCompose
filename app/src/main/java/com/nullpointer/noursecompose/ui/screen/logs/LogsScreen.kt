package com.nullpointer.noursecompose.ui.screen.logs

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.nullpointer.noursecompose.presentation.LogViewModel
import com.nullpointer.noursecompose.ui.screen.logs.componets.ItemLog
import com.nullpointer.noursecompose.ui.share.mpGraph.ToolbarBack
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun LogsScreens(
    logViewModel: LogViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val listLogs = logViewModel.listLogs.collectAsState().value
    Scaffold(
        topBar = {
            ToolbarBack(title = "Registros de las alarmas", navigator::popBackStack
            )
        }
    ) {
        LazyColumn {
            items(listLogs.size) { index ->
                ItemLog(registry = listLogs[index])
            }
        }
    }
}