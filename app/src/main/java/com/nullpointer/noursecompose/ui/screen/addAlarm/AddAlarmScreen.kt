package com.nullpointer.noursecompose.ui.screen.addAlarm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.nullpointer.noursecompose.ui.share.backHandler.BackHandler
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
@Destination
fun AddAlarmScreen() {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val changePage: (value: Int) -> Unit = {
        scope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage + it)
        }
    }

    BackHandler(pagerState.currentPage != 0) {
        changePage(-1)
    }

    HorizontalPager(count = 4, state = pagerState) { page ->
        when (page) {
            0 -> NameAndImgScreen { changePage(+1) }
            1 -> InstruccionScreen { changePage(+1) }
            2 -> RepeatAlarmScreen{ changePage(+1) }
            3 -> TimeScreen()
        }
    }

}