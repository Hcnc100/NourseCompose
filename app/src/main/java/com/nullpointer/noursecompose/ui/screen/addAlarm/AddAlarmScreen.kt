package com.nullpointer.noursecompose.ui.screen.addAlarm

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.nullpointer.noursecompose.R
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

    Box {
        HorizontalPager(count = 4, state = pagerState) { page ->
            when (page) {
                0 -> NameAndImgScreen()
                1 -> InstruccionScreen()
                2 -> RepeatAlarmScreen()
                3 -> TimeScreen()
            }
        }
        Button(onClick = { changePage(+1) }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(10.dp)) {
            Text("Siguiente")
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