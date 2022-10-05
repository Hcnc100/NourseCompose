package com.nullpointer.noursecompose.ui.screen.addAlarm

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    addAlarmScreenState: AddAlarmScreenState = rememberAddAlarmScreenState(
        actionUpdatePickerTime = addAlarmViewModel.alarmTime::changeTimeInitAlarm,
        actionUpdateDateRange = addAlarmViewModel.alarmTime::changeRangeAlarm,
        timeInitAlarm = addAlarmViewModel.alarmTime.timeInitAlarm,
        rangeInitAlarm = addAlarmViewModel.alarmTime.rangeAlarm
    )
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
                CHANGE_VISIBLE_MODAL -> change?.let { addAlarmScreenState.changeVisibleModal(it) }
                CHANGE_VISIBLE_REPEAT -> change?.let {
                    addAlarmViewModel.changeVisibleDialogRepeat(
                        it
                    )
                }
                ACTION_BACK -> actionRootDestinations.backDestination()
                SHOW_TIME_PICKER -> addAlarmScreenState.showTimePicker()
                SHOW_DATE_RANGE_PICKER -> addAlarmScreenState.showDateRangePicker()
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
    actionAddScreen: (ActionAddScreen, Boolean?) -> Unit,
) {
    ScaffoldModal(
        sheetState = sheetState,
        isVisibleModal = isVisibleModal,
        callBackSelection = callBackSelection,
        actionHideModal = { actionAddScreen(CHANGE_VISIBLE_MODAL, false) },
        topBar = { IconClose(actionClick = { actionAddScreen(ACTION_BACK, false) }) }
    ) { padding ->
        BoxWithConstraints {
            val realHeight = remember { this.maxHeight }
            Box(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                HorizontalPager(
                    count = 4,
                    state = pagerState,
                    userScrollEnabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(realHeight)
                        .padding(10.dp)
                ) { page ->
                    when (page) {
                        0 -> NameAndImgScreen(
                            actionNext = { actionAddScreen(NEXT_PAGE, false) },
                            nameAlarmProperty = nameAlarmProperty,
                            imageAlarmProperty = imageAlarmProperty,
                            showModalSelectImg = { actionAddScreen(CHANGE_VISIBLE_MODAL, true) },
                        )
                        1 -> DescriptionScreen(descriptionProperty = descriptionProperty)
                        2 -> RepeatAlarmScreen(alarmTime = timeProperty)
                        3 -> TimeScreen(
                            alarmTime = timeProperty,
                            isShowDialogRepeat = isShowDialogRepeat,
                            showPickerTime = { actionAddScreen(SHOW_TIME_PICKER, null) },
                            changeShowDialogRepeat = { actionAddScreen(CHANGE_VISIBLE_REPEAT, it) },
                            showDateRangePicker = { actionAddScreen(SHOW_DATE_RANGE_PICKER, null) },
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
}


@Composable
private fun IconClose(
    actionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = actionClick,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clear),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ButtonNext(
    actionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        onClick = actionClick
    ) {
        Text(text = stringResource(id = R.string.text_button_next))
    }
}

@Composable
fun TitleAddAlarm(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.h5.copy(fontSize = 22.sp),
        modifier = modifier
    )
}