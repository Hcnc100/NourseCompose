package com.nullpointer.noursecompose.ui.states

import android.content.Context
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
class AddAlarmScreenState(
    context: Context,
    val pagerState:PagerState,
    focusManager: FocusManager,
    val coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    val modalBottomSheetState: ModalBottomSheetState
):SimpleScreenState(scaffoldState, context, focusManager){

    val currentPage get() = pagerState.currentPage
    val isShowModal get() = modalBottomSheetState.isVisible

    fun hiddenModal(){
        coroutineScope.launch {
            modalBottomSheetState.hide()
        }
    }

    fun showModal(){
        coroutineScope.launch {
            hiddenKeyBoard()
            modalBottomSheetState.show()
        }
    }

    fun nextPage(){
        coroutineScope.launch {
            hiddenKeyBoard()
            pagerState.scrollToPage(pagerState.currentPage + 1)
        }
    }

    fun previousPage(){
        coroutineScope.launch {
            hiddenKeyBoard()
            pagerState.scrollToPage(pagerState.currentPage - 1)
        }
    }
}


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun rememberAddAlarmScreenState(
    context: Context=LocalContext.current,
    pagerState: PagerState= rememberPagerState(),
    focusManager: FocusManager= LocalFocusManager.current,
    coroutineScope: CoroutineScope= rememberCoroutineScope(),
    scaffoldState: ScaffoldState= rememberScaffoldState(),
    modalBottomSheetState: ModalBottomSheetState= rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
)= remember(pagerState,scaffoldState,modalBottomSheetState,coroutineScope) {
    AddAlarmScreenState(context, pagerState, focusManager, coroutineScope, scaffoldState, modalBottomSheetState)
}