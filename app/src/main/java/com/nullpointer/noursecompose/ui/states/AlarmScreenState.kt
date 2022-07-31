package com.nullpointer.noursecompose.ui.states

import android.content.Context
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

class AlarmScreenState(
    context: Context,
    scaffoldState: ScaffoldState,
     val lazyGridState: LazyGridState
) : SimpleScreenState(scaffoldState, context) {
    val isScrollInProgress get() = lazyGridState.isScrollInProgress
}

@Composable
fun rememberAlarmScreenState(
    context: Context = LocalContext.current,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    lazyGridState: LazyGridState = rememberLazyGridState()
) = remember(scaffoldState, lazyGridState) {
    AlarmScreenState(context, scaffoldState, lazyGridState)
}