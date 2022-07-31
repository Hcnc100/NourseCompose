package com.nullpointer.noursecompose.ui.screen.addAlarm

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.nullpointer.noursecompose.core.utils.shareViewModel
import com.nullpointer.noursecompose.presentation.AlarmViewModel
import com.nullpointer.noursecompose.ui.navigation.MainNavGraph
import com.nullpointer.noursecompose.ui.screen.addAlarm.descriptionScreen.DescriptionScreen
import com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen.NameAndImgScreen
import com.nullpointer.noursecompose.ui.screen.addAlarm.repeatScreen.RepeatAlarmScreen
import com.nullpointer.noursecompose.ui.screen.addAlarm.timeScreen.TimeScreen
import com.nullpointer.noursecompose.ui.screen.addAlarm.viewModel.AddAlarmViewModel
import com.nullpointer.noursecompose.ui.share.SelectImgButtonSheet
import com.nullpointer.noursecompose.ui.states.AddAlarmScreenState
import com.nullpointer.noursecompose.ui.states.rememberAddAlarmScreenState
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@MainNavGraph
@Composable
@Destination
fun AddAlarmScreen(
    alarmViewModel: AlarmViewModel = shareViewModel(),
    addAlarmViewModel: AddAlarmViewModel = shareViewModel(),
    addAlarmScreenState: AddAlarmScreenState = rememberAddAlarmScreenState()
) {
    BackHandler(addAlarmScreenState.currentPage != 0, addAlarmScreenState::previousPage)

    Scaffold(
//        topBar = {
//            ToolbarBack(title = if (alarm == null) stringResource(R.string.title_new_alarm) else stringResource(
//                R.string.title_edit_alarm),
//                actionBack = navigator::popBackStack)
//        }
    ) {

        ModalBottomSheetLayout(
            sheetState = addAlarmScreenState.modalBottomSheetState,
            sheetContent = {
                SelectImgButtonSheet(
                    actionHidden = addAlarmScreenState::hiddenModal,
                    isVisible = addAlarmScreenState.isShowModal
                ) { uri ->
                    addAlarmScreenState.hiddenModal()
                    uri?.let {
                        addAlarmViewModel.imageAlarm.changeValue(
                            it,
                            addAlarmScreenState.context
                        )
                    }
                }
            },
        ) {


            Box(modifier = Modifier.padding(it)) {
                HorizontalPager(
                    count = 4,
                    state = addAlarmScreenState.pagerState,
                    userScrollEnabled = false
                ) { page ->
                    when (page) {
                        0 -> NameAndImgScreen(
                            addAlarmViewModel = addAlarmViewModel,
                            addAlarmScreenState = addAlarmScreenState
                        )
                        1 -> DescriptionScreen(addAlarmViewModel = addAlarmViewModel)
                        2 -> RepeatAlarmScreen(addAlarmViewModel = addAlarmViewModel)
                        3 -> TimeScreen(addAlarmViewModel = addAlarmViewModel)
                    }
                }
                ExtendedFloatingActionButton(
                    modifier = Modifier.padding(20.dp).align(Alignment.BottomEnd),
                    text = { Text(text = "Next") },
                    onClick = {
                        when (addAlarmScreenState.currentPage) {
                            0 -> {
                                addAlarmViewModel.nameAlarm.autoValue()
                                if (!addAlarmViewModel.nameAlarm.hasError)
                                    addAlarmScreenState.nextPage()
                            }
                            1, 2 -> addAlarmScreenState.nextPage()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TitleAddAlarm(
    title: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.h5,
        modifier = Modifier.padding(30.dp)
    )
}