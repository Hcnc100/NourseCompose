package com.nullpointer.noursecompose.ui.screen.addAlarm

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.actions.ActionAddScreen
import com.nullpointer.noursecompose.actions.ActionAddScreen.*
import com.nullpointer.noursecompose.core.delegates.PropertySavableAlarmTime
import com.nullpointer.noursecompose.core.delegates.PropertySavableImg
import com.nullpointer.noursecompose.core.delegates.PropertySavableString
import com.nullpointer.noursecompose.core.utils.shareViewModel
import com.nullpointer.noursecompose.presentation.AlarmViewModel
import com.nullpointer.noursecompose.ui.interfaces.ActionRootDestinations
import com.nullpointer.noursecompose.ui.navigation.MainNavGraph
import com.nullpointer.noursecompose.ui.screen.addAlarm.descriptionScreen.DescriptionScreen
import com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen.NameAndImgScreen
import com.nullpointer.noursecompose.ui.screen.addAlarm.repeatScreen.RepeatAlarmScreen
import com.nullpointer.noursecompose.ui.screen.addAlarm.timeScreen.TimeScreen
import com.nullpointer.noursecompose.ui.screen.addAlarm.viewModel.AddAlarmViewModel
import com.nullpointer.noursecompose.ui.share.ScaffoldModal
import com.nullpointer.noursecompose.ui.share.ToolbarBack
import com.nullpointer.noursecompose.ui.states.AddAlarmScreenState
import com.nullpointer.noursecompose.ui.states.rememberAddAlarmScreenState
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(
    ExperimentalPagerApi::class, ExperimentalMaterialApi::class, ExperimentalMaterialApi::class
)
@MainNavGraph
@Composable
@Destination
fun AddAlarmScreen(
    actionRootDestinations: ActionRootDestinations,
    alarmViewModel: AlarmViewModel = shareViewModel(),
    addAlarmViewModel: AddAlarmViewModel = shareViewModel(),
    addAlarmScreenState: AddAlarmScreenState = rememberAddAlarmScreenState()
) {
    BackHandler(addAlarmScreenState.currentPage != 0, addAlarmScreenState::previousPage)

    AddAlarmScreen(
        timeProperty = addAlarmViewModel.alarmTime,
        pagerState = addAlarmScreenState.pagerState,
        nameAlarmProperty = addAlarmViewModel.nameAlarm,
        isVisibleModal = addAlarmScreenState.isShowModal,
        imageAlarmProperty = addAlarmViewModel.imageAlarm,
        descriptionProperty = addAlarmViewModel.description,
        sheetState = addAlarmScreenState.modalBottomSheetState,
        isShowDialogRepeat = addAlarmViewModel.showDialogRepeat,
        callBackSelection = addAlarmViewModel.imageAlarm::changeValue,
        actionAddScreen = { action, change ->
            when (action) {
                ACTION_BACK -> actionRootDestinations.backDestination()
                CHANGE_VISIBLE_MODAL -> addAlarmScreenState.changeVisibleModal(change)
                CHANGE_VISIBLE_REPEAT -> addAlarmViewModel.changeVisibleDialogRepeat(change)
                NEXT_PAGE -> {
                    when (addAlarmScreenState.currentPage) {
                        0 -> addAlarmViewModel.valueNameAlarm(addAlarmScreenState::nextPage)
                        1, 2 -> addAlarmScreenState.nextPage()
                        3 -> {
                            addAlarmViewModel.valueAlarmTime(alarmViewModel::addNewAlarm)
                            actionRootDestinations.backDestination()
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun AddAlarmScreen(
    isVisibleModal: Boolean,
    pagerState: PagerState,
    isShowDialogRepeat: Boolean,
    callBackSelection: (Uri) -> Unit,
    sheetState: ModalBottomSheetState,
    timeProperty: PropertySavableAlarmTime,
    imageAlarmProperty: PropertySavableImg,
    nameAlarmProperty: PropertySavableString,
    descriptionProperty: PropertySavableString,
    actionAddScreen: (ActionAddScreen, Boolean) -> Unit
) {
    ScaffoldModal(
        isVisibleModal = isVisibleModal,
        actionHideModal = { actionAddScreen(CHANGE_VISIBLE_MODAL, false) },
        callBackSelection = callBackSelection,
        sheetState = sheetState,
        topBar = {
            ToolbarBack(
                title = stringResource(R.string.title_new_alarm),
                actionBack = { actionAddScreen(ACTION_BACK, false) }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HorizontalPager(
                count = 4,
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    0 -> NameAndImgScreen(
                        actionNext = { actionAddScreen(NEXT_PAGE, false) },
                        modifier = Modifier.imePadding(),
                        nameAlarmProperty = nameAlarmProperty,
                        imageAlarmProperty = imageAlarmProperty,
                        showModalSelectImg = { actionAddScreen(CHANGE_VISIBLE_MODAL, true) },
                    )
                    1 -> DescriptionScreen(
                        descriptionProperty = descriptionProperty,
                        modifier = Modifier.imePadding()
                    )
                    2 -> RepeatAlarmScreen(alarmTime = timeProperty)
                    3 -> TimeScreen(
                        alarmTime = timeProperty,
                        isShowDialogRepeat = isShowDialogRepeat,
                        changeShowDialogRepeat = { actionAddScreen(CHANGE_VISIBLE_REPEAT, it) },
                    )
                }
            }
            ButtonNext(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.BottomEnd),
                actionClick = { actionAddScreen(NEXT_PAGE, false) },
            )
        }
    }
}


@Composable
private fun ButtonNext(
    actionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        modifier = modifier,
        text = { Text(text = stringResource(id = R.string.text_button_next)) },
        onClick = actionClick
    )
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