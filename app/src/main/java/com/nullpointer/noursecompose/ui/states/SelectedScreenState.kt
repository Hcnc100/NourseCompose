package com.nullpointer.noursecompose.ui.states

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.nullpointer.noursecompose.core.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SelectedScreenState(
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

    fun showToast(@StringRes messageRes: Int) {
        context.showToast(messageRes)
    }

}

@Composable
fun rememberSelectedScreenState(
    context: Context = LocalContext.current,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) = remember(scaffoldState, coroutineScope, lazyGridState) {
    SelectedScreenState(context, scaffoldState, lazyGridState, coroutineScope)
}