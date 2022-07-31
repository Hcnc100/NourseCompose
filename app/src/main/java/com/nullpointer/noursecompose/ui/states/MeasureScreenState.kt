package com.nullpointer.noursecompose.ui.states

import android.content.Context
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MeasureScreenState(
    context: Context,
    scaffoldState: ScaffoldState,
    val lazyGridState: LazyGridState,
    val coroutineScope: CoroutineScope,
) : SimpleScreenState(scaffoldState, context) {

    val isScrollInProgress get() = lazyGridState.isScrollInProgress

    fun scrollToFirst() {
        coroutineScope.launch {
            lazyGridState.animateScrollToItem(0)
        }
    }
}

@Composable
fun rememberMeasureScreenState(
    context: Context = LocalContext.current,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) = remember(scaffoldState, coroutineScope, lazyGridState) {
    MeasureScreenState(context, scaffoldState, lazyGridState, coroutineScope)
}