package com.nullpointer.noursecompose.ui.screen.addAlarm

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.imePadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.models.alarm.Alarm
import com.nullpointer.noursecompose.models.alarm.AlarmTypes
import com.nullpointer.noursecompose.presentation.AlarmViewModel
import com.nullpointer.noursecompose.ui.screen.addAlarm.descriptionScreen.DescriptionScreen
import com.nullpointer.noursecompose.ui.screen.addAlarm.descriptionScreen.viewModel.DescriptionViewModel
import com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen.NameAndImgScreen
import com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen.viewModel.NameAndImgViewModel
import com.nullpointer.noursecompose.ui.screen.addAlarm.repeatScreen.RepeatAlarmScreen
import com.nullpointer.noursecompose.ui.screen.addAlarm.timeScreen.TimeScreen
import com.nullpointer.noursecompose.ui.screen.addAlarm.timeScreen.viewModel.TimeViewModel
import com.nullpointer.noursecompose.ui.share.backHandler.BackHandler
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
@Destination
fun AddAlarmScreen(
    nameAndImgViewModel: NameAndImgViewModel = hiltViewModel(),
    descriptionViewModel: DescriptionViewModel = hiltViewModel(),
    timeViewModel: TimeViewModel = hiltViewModel(),
    alarmViewModel: AlarmViewModel,
    navigator: DestinationsNavigator,
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val modalState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val changePage: (value: Int) -> Unit = {
        focusManager.clearFocus()
        scope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage + it)
        }
    }

    BackHandler(pagerState.currentPage != 0) {
        changePage(-1)
    }

    Box {
        HorizontalPager(count = 4, state = pagerState, userScrollEnabled = false) { page ->
            when (page) {
                0 -> NameAndImgScreen(nameAndImgViewModel,
                    modalState, focusManager)
                1 -> DescriptionScreen(descriptionViewModel, focusManager)
                2 -> RepeatAlarmScreen(timeViewModel)
                3 -> TimeScreen(timeViewModel)
            }
        }
        AnimatedVisibility(visible = !modalState.isVisible,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp),
            enter = slideInHorizontally { it } + fadeIn(),
            exit = shrinkHorizontally { it } + fadeOut()) {
            Button(onClick = {
                when (pagerState.currentPage) {
                    0 -> {
                        nameAndImgViewModel.validateName()
                        if (!nameAndImgViewModel.hasErrorName) {
                            changePage(+1)
                        }
                    }
                    1 -> {
                        descriptionViewModel.validateDescription()
                        if (!descriptionViewModel.hasErrorDescription) {
                            changePage(+1)
                        }
                    }
                    2 -> changePage(+1)
                    3 -> {
                        if (timeViewModel.typeAlarm != AlarmTypes.RANGE || timeViewModel.typeAlarm == AlarmTypes.RANGE && !timeViewModel.hasErrorRange) {
                            val (timeInit, timeFinish) = if (timeViewModel.typeAlarm == AlarmTypes.RANGE) {
                                timeViewModel.rangeAlarm
                            } else {
                                Pair(null, null)
                            }
                            alarmViewModel.addNewAlarm(
                                file = nameAndImgViewModel.fileImg,
                                context = context,
                                alarm = Alarm(
                                    title = nameAndImgViewModel.nameAlarm,
                                    message = descriptionViewModel.description,
                                    typeAlarm = timeViewModel.typeAlarm,
                                    nextAlarm = timeViewModel.timeNextAlarm,
                                    repeaterEvery = timeViewModel.timeToRepeatAlarm,
                                    rangeInitAlarm = timeInit,
                                    rangeFinishAlarm = timeFinish
                                )
                            )
                            navigator.popBackStack()
                        }
                    }
                }
            }) {
                Text(stringResource(R.string.text_button_next))
            }
        }
    }

}

@Composable
fun ContentPage(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {


        Text(text = title,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(30.dp))

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            content()
        }

        Box(modifier = Modifier
            .height(150.dp)
            .fillMaxWidth())

    }

}