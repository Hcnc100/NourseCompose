package com.nullpointer.noursecompose.ui.screen.addAlarm

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.nullpointer.noursecompose.R
import com.nullpointer.noursecompose.ui.screen.addAlarm.descriptionScreen.DescriptionScreen
import com.nullpointer.noursecompose.ui.screen.addAlarm.descriptionScreen.viewModel.DescriptionViewModel
import com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen.NameAndImgScreen
import com.nullpointer.noursecompose.ui.screen.addAlarm.nameScreen.viewModel.NameAndImgViewModel
import com.nullpointer.noursecompose.ui.share.backHandler.BackHandler
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
@Destination
fun AddAlarmScreen(
    nameAndImgViewModel: NameAndImgViewModel = hiltViewModel(),
    descriptionViewModel: DescriptionViewModel = hiltViewModel(),
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val modalState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val changePage: (value: Int) -> Unit = {
        scope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage + it)
        }
    }

    BackHandler(pagerState.currentPage != 0) {
        changePage(-1)
    }

    Box {
        HorizontalPager(count = 4, state = pagerState) { page ->
            when (page) {
                0 -> NameAndImgScreen(nameAndImgViewModel, modalState)
                1 -> DescriptionScreen(descriptionViewModel)
                2 -> RepeatAlarmScreen()
                3 -> TimeScreen()
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
                    1->{
                        descriptionViewModel.validateDescription()
                        if(!descriptionViewModel.hasErrorDescription){
                            changePage(+1)
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
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween) {

        Text(text = title,
            style = MaterialTheme.typography.h5, modifier = Modifier
                .padding(30.dp)
                .weight(1f))

        Box(modifier = Modifier
            .fillMaxSize()
            .weight(2f)) {
            content()
        }
        Spacer(modifier = Modifier.weight(1f))

    }

}